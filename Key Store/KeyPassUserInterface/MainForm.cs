using KeyPassBusiness;
using KeyPassInfoObjects;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml;

namespace KeyPassUserInterface
{
    public partial class MainForm : Form
    {

        #region Non-Public Data Members

        private string FileFilter = "KeyPass Files | *.xml";
        private string DefaultExt = ".xml";
        private string ApplicationName = "My Key Pass";
        private PrintDocument _printDoc = new PrintDocument();

        #endregion

        #region Non-Public Properties/Methods

        private void Print(PrinterSettings psetting)
        {
            _printDoc.PrinterSettings = psetting;
            _printDoc.Print();
        }

        private void OnPrintPage(object sender, PrintPageEventArgs e)
        {
            Graphics g = e.Graphics;
            g.TranslateTransform(e.MarginBounds.X, e.MarginBounds.Y);
            Draw(g, e.MarginBounds.Width, e.MarginBounds.Height);
        }

        private void Draw(Graphics g, int pageWidth, int pageHeight)
        {
            int cellHeight = 25;
            int numCols = 6;
            int cellWidth = pageWidth / numCols;
            int numRows = pageHeight / cellHeight;
            Font font = new Font("Times New Roman", 11);
            StringFormat strFormat = new StringFormat();
            strFormat.LineAlignment = StringAlignment.Center;
            strFormat.Trimming = StringTrimming.EllipsisCharacter;
            strFormat.FormatFlags = StringFormatFlags.NoWrap;
            RectangleF rectF = new RectangleF(0, 0, cellWidth, cellHeight);

            DrawCell(g, "Group", font, strFormat, rectF);
            rectF.X += cellWidth;
            DrawCell(g, "Title", font, strFormat, rectF);
            rectF.X += cellWidth;
            DrawCell(g, "Username", font, strFormat, rectF);
            rectF.X += cellWidth;
            DrawCell(g, "Password", font, strFormat, rectF);
            rectF.X += cellWidth;
            DrawCell(g, "Url", font, strFormat, rectF);
            rectF.X += cellWidth;
            DrawCell(g, "Notes", font, strFormat, rectF);
            rectF.X = 0;
            rectF.Y += cellHeight;

            foreach (Group group in KeyPassMgr.Document.Groups)
            {
                DrawCell(g, group.Name, font, strFormat, rectF);
                rectF.Y += cellHeight;
                if (rectF.Y >= pageHeight)
                {
                    return;
                }

                foreach (Key key in group.Keys)
                {
                    rectF.X += cellWidth;
                    DrawCell(g, key.Title, font, strFormat, rectF);
                    rectF.X += cellWidth;
                    DrawCell(g, key.Username, font, strFormat, rectF);
                    rectF.X += cellWidth;
                    DrawCell(g, key.Password, font, strFormat, rectF);
                    rectF.X += cellWidth;
                    DrawCell(g, key.Url, font, strFormat, rectF);
                    rectF.X += cellWidth;
                    DrawCell(g, key.Notes, font, strFormat, rectF);
                    rectF.X = 0;
                    rectF.Y += cellHeight;
                    if (rectF.Y >= pageHeight)
                    {
                        return;
                    }
                }
            }
        }

        private void DrawCell(Graphics g, string text, Font font, StringFormat strFormat, RectangleF rectF)
        {
            g.DrawString(text, font, new SolidBrush(Color.Black), rectF, strFormat);
            g.DrawRectangle(Pens.DarkSlateGray, rectF.X, rectF.Y, rectF.Width, rectF.Height);
        }

        private bool PromptSave(object sender, EventArgs e)
        {
            if (KeyPassMgr.Document.IsModified)
            {
                DialogResult res = MessageBox.Show(this, "Current document has unsaved changes. Save changes?", ApplicationName,
                    MessageBoxButtons.YesNoCancel, MessageBoxIcon.Question);
                if (res == DialogResult.No)
                    return true;

                if (res == DialogResult.Yes)
                    return Save();

                if (res == DialogResult.Cancel)
                    return false;
            }

            // Continue
            return true;
        }
        
        private bool Save()
        {
            if (!KeyPassMgr.Document.IsNamed)
            {
                return SaveAs();
            }

            KeyPassMgr.Save();
            return true;
        }

        private bool SaveAs()
        {
            SaveFileDialog d = new SaveFileDialog();
            d.Filter = FileFilter;
            d.DefaultExt = DefaultExt;
            using (d)
            {
                if (d.ShowDialog(this) != DialogResult.OK)
                {
                    return false;
                }

                KeyPassMgr.SaveAs(d.FileName);
                return true;
            }
        }

        private void New(KeyPassDocument doc)
        {
            _listViewKeys.BeginUpdate();
            _listViewKeys.Items.Clear();
            _listViewKeys.EndUpdate();

            _treeViewGroups.BeginUpdate();
            _treeViewGroups.Nodes.Clear();
            _treeViewGroups.EndUpdate();

            _keyDetailsTextBox.Visible = false;
            
        }

        private void Open(KeyPassDocument doc)
        {
            _treeViewGroups.BeginUpdate();
            _treeViewGroups.Nodes.Clear();
            foreach (Group g in doc.Groups)
            {
                TreeNode node = _treeViewGroups.Nodes.Add(g.Name);
                node.Tag = g;
                ContextMgr.SelectedGroup = g;
                _treeViewGroups.SelectedNode = node;
            }
            _treeViewGroups.EndUpdate();

            _keyDetailsTextBox.Visible = false;
        }

        #endregion

        #region Public Interface

        public MainForm()
        {
            InitializeComponent();
            
        }

        #endregion

        #region Event Handlers
        private void OnLoad(object sender, EventArgs e)
        {
            if (DesignMode)
                return;

            _listViewKeys.Columns.Add("Title");
            _listViewKeys.Columns.Add("User Name");
            _listViewKeys.Columns.Add("Password");
            _listViewKeys.Columns.Add("URL");

            Application.Idle += OnIdle;
            KeyPassMgr.DocumentLoaded += OnDocumentLoaded;
            KeyPassMgr.DocumentSaved += OnDocumentSaved;

            _printDoc.PrintPage += OnPrintPage;
        }

        private void OnDocumentLoaded(KeyPassDocument doc)
        {
            ContextMgr.Initialize();

            if (doc.IsNamed)
            {
                Open(doc);
                Text = doc.Filename;
            }
            else
            {
                New(doc);
                Text = ApplicationName;
            }
        }

        private void OnDocumentSaved(KeyPassDocument doc)
        {
            Text = doc.Filename;
        }

        private void OnIdle(object sender, EventArgs e)
        {
            editGroupToolStripButton.Enabled = ContextMgr.SelectedGroup != null;
            deleteGroupToolStripButton.Enabled = ContextMgr.SelectedGroup != null;
            editEntryToolStripButton.Enabled = ContextMgr.SelectedKeys.Count == 1;
            deleteEntryToolStripButton.Enabled = ContextMgr.SelectedKeys.Count != 0;
            saveToolStripButton.Enabled = KeyPassMgr.Document.IsModified && !KeyPassMgr.Document.IsEmpty;

            if (_treeViewGroups.Nodes.Count == 0)
            {
                _listViewKeys.Items.Clear();
                ContextMgr.Initialize();
            }

            if (_listViewKeys.Items.Count == 0)
            {
                _listViewKeys.AutoResizeColumns(ColumnHeaderAutoResizeStyle.HeaderSize);
                ContextMgr.SelectedKeys.Clear();
            }
        }

        private void OnContextMenuOpening(object sender, CancelEventArgs e)
        {
            editGroupContextMenuItem.Enabled = ContextMgr.SelectedGroup != null;
            deleteGroupContextMenuItem.Enabled = ContextMgr.SelectedGroup != null;
        }

        private void _OnContextMenu2Opening(object sender, CancelEventArgs e)
        {
            editEntryToolStripMenuItem1.Enabled = ContextMgr.SelectedKeys.Count == 1;
            duplicateEntryToolStripMenuItem1.Enabled = ContextMgr.SelectedKeys.Count != 0;
            deleteEntryToolStripMenuItem1.Enabled = ContextMgr.SelectedKeys.Count != 0;
        }

        private void OnEditOpening(object sender, EventArgs e)
        {
            editGroupToolStripMenuItem.Enabled = ContextMgr.SelectedGroup != null;
            deleteGroupToolStripMenuItem.Enabled = ContextMgr.SelectedGroup != null;
            addEntryToolStripMenuItem.Enabled = ContextMgr.SelectedGroup != null;
            editEntryToolStripMenuItem.Enabled = ContextMgr.SelectedKeys.Count == 1;
            deleteEntryToolStripMenuItem.Enabled = ContextMgr.SelectedKeys.Count != 0;
            duplicateEntryToolStripMenuItem.Enabled = ContextMgr.SelectedKeys.Count != 0;
        }

        private void OnKeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Delete)
            {
                if (ContextMgr.SelectedKeys.Count > 0)
                {
                    deleteEntryToolStripButton.PerformClick();
                }

            }

            if (e.Control && e.KeyCode == Keys.D)
            {
                OnDuplicateEntry(sender, e);
            }
        }

        private void OnKeyDownGroup(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Delete)
            {
                if (ContextMgr.SelectedGroup != null)
                {
                    deleteGroupToolStripButton.PerformClick();
                }

            }

            if (e.Control && e.KeyCode == Keys.C)
            {
                OnGroupCopy();
            }

            if (e.Control && e.KeyCode == Keys.V)
            {
                OnGroupPaste();
            }
        }

        private void OnGroupCopy()
        {
            if (ContextMgr.SelectedGroup != null)
            {
                DataObject dataobj = new DataObject();
                dataobj.SetData("group", ContextMgr.SelectedGroup);
                Clipboard.SetDataObject(dataobj, true);
            }
        }

        private void OnGroupPaste()
        {
            IDataObject obj = Clipboard.GetDataObject();
            if (obj.GetDataPresent("group"))
            {
                Group g = (Group)Clipboard.GetData("group");
                Group newg = KeyPassMgr.DuplicateGroup(g);

                TreeNode node = _treeViewGroups.Nodes.Add(newg.Name);
                node.Tag = newg;
                ContextMgr.Initialize();
                ContextMgr.SelectedGroup = newg;
                _treeViewGroups.SelectedNode = node;

                string record = DateTime.Now.ToString() + "-Add Group: " + newg.Name;
                _statusControl.AddAuditTrail(record);
            }
        }

        private void OnFormClosing(object sender, FormClosingEventArgs e)
        {
            // if user pressed cancel, then cancel closing
            if (!PromptSave(sender, e))
                e.Cancel = true;
        }

        #endregion

        #region File Commands
        private void OnFileOpening(object sender, EventArgs e)
        {
            saveToolStripMenuItem.Enabled = KeyPassMgr.Document.IsModified && !KeyPassMgr.Document.IsEmpty;
            saveAsToolStripMenuItem.Enabled = KeyPassMgr.Document.IsModified;
        }

        private void OnFileNew(object sender, EventArgs e)
        {
            if (!PromptSave(sender, e))
                return;

            KeyPassMgr.New();
        }

        private void OnFileOpen(object sender, EventArgs e)
        {
            if (!PromptSave(sender, e))
                return;

            OpenFileDialog d = new OpenFileDialog();
            d.Filter = FileFilter;
            using (d)
            {
                if (d.ShowDialog(this) != DialogResult.OK)
                {
                    return;
                }

                try
                {
                    KeyPassMgr.Open(d.FileName);
                }
                catch (Exception ex)
                {
                    MessageBox.Show(this, ex.Message, ApplicationName, MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }
            }
            
        }

        private void OnFileSave(object sender, EventArgs e)
        {
            Save();
        }

        private void OnFileSaveAs(object sender, EventArgs e)
        {
            SaveAs();
        }

        private void OnFilePrint(object sender, EventArgs e)
        {
            PrintDialog d = new PrintDialog();
            using (d)
            {
                if (d.ShowDialog(this) != DialogResult.OK)
                {
                    return;
                }
                Print(d.PrinterSettings);
            }
        }

        private void OnFilePrintPreview(object sender, EventArgs e)
        {
            PrintPreviewDialog d = new PrintPreviewDialog();
            d.WindowState = FormWindowState.Maximized;
            d.Document = _printDoc;
            using (d)
            {
                if (d.ShowDialog(this) != DialogResult.OK)
                {
                    return;
                }
            }
        }

        private void OnFileExit(object sender, EventArgs e)
        {
            this.Close();
        }

        #endregion

        #region Edit Commands

        private void OnAddGroup(object sender, EventArgs e)
        {
            GroupForm f = new GroupForm();
            if (f.ShowDialog(this) != DialogResult.OK)
                return;

            Group g = f.Group;
            KeyPassMgr.AddGroup(g);

            TreeNode node = _treeViewGroups.Nodes.Add(g.Name);
            node.Tag = g;
            ContextMgr.SelectedGroup = g;
            _treeViewGroups.SelectedNode = node;

            string record = DateTime.Now.ToString() + "-Add Group: " + g.Name;
            _statusControl.AddAuditTrail(record);
        }

        private void OnEditGroup(object sender, EventArgs e)
        {
            GroupForm f = new GroupForm();
            f.Edit = true;
            if (f.ShowDialog(this) != DialogResult.OK)
                return;

            KeyPassMgr.EditGroup(ContextMgr.SelectedGroup, f.Group.Name);
            _treeViewGroups.SelectedNode.Text = f.Group.Name;

            string record = DateTime.Now.ToString() + "-Edit Group: " + f.Group.Name;
            _statusControl.AddAuditTrail(record);
        }

        private void OnEditGroup(object sender, TreeNodeMouseClickEventArgs e)
        {
            GroupForm f = new GroupForm();
            f.Edit = true;
            if (f.ShowDialog(this) != DialogResult.OK)
                return;

            KeyPassMgr.EditGroup(ContextMgr.SelectedGroup, f.Group.Name);
            _treeViewGroups.SelectedNode.Text = f.Group.Name;

            string record = DateTime.Now.ToString() + "-Edit Group: " + f.Group.Name;
            _statusControl.AddAuditTrail(record);

        }

        private void OnDeleteGroup(object sender, EventArgs e)
        {
            DialogResult result = MessageBox.Show(this, "This will delete the group and all of its associated keys. Are you sure?", "My Key Pass", MessageBoxButtons.YesNo);
            if (result == DialogResult.Yes)
            {
                string Name = ContextMgr.SelectedGroup.Name;
                KeyPassMgr.DeleteGroup(ContextMgr.SelectedGroup);
                _treeViewGroups.Nodes.Remove(_treeViewGroups.SelectedNode);
                _keyDetailsTextBox.Visible = false;

                string record = DateTime.Now.ToString() + "-Delete Group: " + Name;
                _statusControl.AddAuditTrail(record);
            }
        }

        private void OnAddEntry(object sender, EventArgs e)
        {
            if (ContextMgr.SelectedGroup == null)
            {
                MessageBox.Show(this, "Please select a group", "My Key Pass");
            }
            else
            {
                List<Group> groups = KeyPassMgr.GetAllGroups();

                KeyForm f = new KeyForm();
                f.SetGroups(groups);
                if (f.ShowDialog(this) != DialogResult.OK)
                    return;

                Key k = f.Key;
                KeyPassMgr.AddKeyToGroup(f.Group, k);

                if (f.Group == ContextMgr.SelectedGroup)
                {
                    ListViewItem lvi = _listViewKeys.Items.Add(k.Title);
                    lvi.SubItems.Add(k.Username);
                    lvi.SubItems.Add(k.Password);
                    lvi.SubItems.Add(k.Url);
                    lvi.Tag = k;
                    _listViewKeys.AutoResizeColumns(ColumnHeaderAutoResizeStyle.ColumnContent);
                }

                string record = DateTime.Now.ToString() + "-Add Key to Group: " + f.Group.Name + " Title=" + k.Title + " Username=" + k.Username + " Password=" + k.Password + " URL=" + k.Url;
                _statusControl.AddAuditTrail(record);

            }
            
        }

        private void OnEditEntry(object sender, EventArgs e)
        {
            List<Group> groups = KeyPassMgr.GetAllGroups();

            KeyForm f = new KeyForm();
            f.SetGroups(groups);
            f.Edit = true;
            if (f.ShowDialog(this) != DialogResult.OK)
                return;

            Key k = f.Key;
            ListViewItem lvi = _listViewKeys.SelectedItems[0];
            if (f.Group == ContextMgr.SelectedGroup)
            {
                KeyPassMgr.EditKey(ContextMgr.SelectedKeys[0], k);
                lvi.SubItems[0].Text = k.Title;
                lvi.SubItems[1].Text = k.Username;
                lvi.SubItems[2].Text = k.Password;
                lvi.SubItems[3].Text = k.Url;
                _listViewKeys.AutoResizeColumns(ColumnHeaderAutoResizeStyle.ColumnContent);
                lvi.Selected = false;
                lvi.Selected = true;
            }
            else
            {
                KeyPassMgr.AddKeyToGroup(f.Group, k);
                KeyPassMgr.DeleteKeyFromGroup(ContextMgr.SelectedGroup, ContextMgr.SelectedKeys[0]);
                _listViewKeys.Items.Remove(lvi);
            }

            string record = DateTime.Now.ToString() + "-Edit Key in Group: " + f.Group.Name + " Title=" + k.Title + " Username=" + k.Username + " Password=" + k.Password + " URL=" + k.Url;
            _statusControl.AddAuditTrail(record);

        }

        private void OnDeleteEntry(object sender, EventArgs e)
        {
            foreach (Key k in ContextMgr.SelectedKeys)
            {
                string record = DateTime.Now.ToString() + "-Delete Key From Group: " + ContextMgr.SelectedGroup.Name + " Title=" + k.Title + " Username=" + k.Username + " Password=" + k.Password + " URL=" + k.Url;
                KeyPassMgr.DeleteKeyFromGroup(ContextMgr.SelectedGroup, k);
                _statusControl.AddAuditTrail(record);
            }
            foreach (ListViewItem lvi in _listViewKeys.SelectedItems)
            {
                _listViewKeys.Items.Remove(lvi);
            }

        }

        private void OnDuplicateEntry(object sender, EventArgs e)
        {
            if (ContextMgr.SelectedKeys.Count != 0)
            {
                Key newk;
                foreach (Key k in ContextMgr.SelectedKeys)
                {
                    newk = KeyPassMgr.DuplicateKey(k);
                    ListViewItem lvi = _listViewKeys.Items.Add(newk.Title);
                    lvi.SubItems.Add(newk.Username);
                    lvi.SubItems.Add(newk.Password);
                    lvi.SubItems.Add(newk.Url);
                    lvi.Tag = newk;
                    _listViewKeys.AutoResizeColumns(ColumnHeaderAutoResizeStyle.ColumnContent);

                    string record = DateTime.Now.ToString() + "-Add Key to Group: " + ContextMgr.SelectedGroup.Name + " Title=" + newk.Title + " Username=" + newk.Username + " Password=" + newk.Password + " URL=" + newk.Url;
                    _statusControl.AddAuditTrail(record);
                }
            }
        }

        private void OnFindEntry(object sender, EventArgs e)
        {

        }

        #endregion

        #region View Commands

        private void OnViewTool(object sender, EventArgs e)
        {
            _toolStrip.Visible = !_toolStrip.Visible;
            toolBarToolStripMenuItem.Checked = !toolBarToolStripMenuItem.Checked;
        }

        private void OnViewStatus(object sender, EventArgs e)
        {
            _statusControl.Visible = !_statusControl.Visible;
            statusBarToolStripMenuItem.Checked = !statusBarToolStripMenuItem.Checked;
        }

        #endregion

        #region Tools Commands

        private void OnCustomize(object sender, EventArgs e)
        {

        }

        private void OnOptions(object sender, EventArgs e)
        {

        }

        #endregion

        #region Help Commands

        private void OnHelpContents(object sender, EventArgs e)
        {

        }

        private void OnHelpAbout(object sender, EventArgs e)
        {
            AboutForm f = new AboutForm();
            f.ShowDialog();
        }

        #endregion

        private void OnSelectGroup(object sender, TreeViewEventArgs e)
        {
            ContextMgr.SelectedGroup = (Group)_treeViewGroups.SelectedNode.Tag;
            List<Key> keys = ContextMgr.SelectedGroup.Keys;
            ListViewItem lvi = null;
            _listViewKeys.Items.Clear();
            _keyDetailsTextBox.Visible = false;

            if (keys.Count != 0)
            {
                foreach (Key k in keys)
                {
                    lvi = _listViewKeys.Items.Add(k.Title);
                    lvi.SubItems.Add(k.Username);
                    lvi.SubItems.Add(k.Password);
                    lvi.SubItems.Add(k.Url);
                    lvi.Tag = k;
                }

                _listViewKeys.AutoResizeColumns(ColumnHeaderAutoResizeStyle.ColumnContent);
            }

        }

        private void OnSelectKey(object sender, EventArgs e)
        {
            ContextMgr.SelectedKeys.Clear();

            foreach (ListViewItem lvi in _listViewKeys.SelectedItems)
            {
                ContextMgr.SelectedKeys.Add((Key)lvi.Tag);
            }

            _keyDetailsTextBox.Visible = ContextMgr.SelectedKeys.Count == 1;

            if (_keyDetailsTextBox.Visible)
            {
                _keyDetailsTextBox.Text = "Title = " + ContextMgr.SelectedKeys[0].Title + "\n"
                    + "User Name = " + ContextMgr.SelectedKeys[0].Username + "\n"
                    + "Password = " + ContextMgr.SelectedKeys[0].Password + "\n"
                    + "URL = " + ContextMgr.SelectedKeys[0].Url + "\n"
                    + "Notes\n------------------------\n" + ContextMgr.SelectedKeys[0].Notes;
            }
        }

        private void OnDragEnter(object sender, DragEventArgs e)
        {
            if (e.Data.GetDataPresent(DataFormats.FileDrop))
            {
                string[] files = (string[])e.Data.GetData(DataFormats.FileDrop, false);
                string myFile = files[0];
                string extension = Path.GetExtension(myFile);
                if (extension == ".xml")
                {
                    e.Effect = DragDropEffects.All;
                }
                else
                {
                    e.Effect = DragDropEffects.None;
                }
            }
            else
                e.Effect = DragDropEffects.None;
        }

        private void OnDragDrop(object sender, DragEventArgs e)
        {
            string[] files = (string[])e.Data.GetData(DataFormats.FileDrop, false);
            string myFile = files[0];
            try
            {
                KeyPassMgr.Open(myFile);
            }
            catch (Exception ex)
            {
                MessageBox.Show(this, ex.Message, ApplicationName, MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
        }
        
        
    }
}
