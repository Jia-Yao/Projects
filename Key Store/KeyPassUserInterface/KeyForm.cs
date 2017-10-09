using KeyPassBusiness;
using KeyPassInfoObjects;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace KeyPassUserInterface
{
    public partial class KeyForm : Form
    {
        private Key _key = new Key();
        private Group _group;
        private List<Group> _groups = null;
        private bool _edit = false;

        public Key Key
        {
            get { return _key; }
            set { _key = value; }
        }

        public Group Group
        {
            get { return _group; }
            set { _group = value; }
        }

        public void SetGroups(List<Group> groups)
        {
            _groups = groups;
        }

        public bool Edit
        {
            get { return _edit; }
            set { _edit = value; }
        }

        public KeyForm()
        {
            InitializeComponent();
        }

        private void OnLoad(object sender, EventArgs e)
        {
            foreach (Group g in _groups)
            {
                _groupComboList.Items.Add(g.Name);
            }
            _groupComboList.SelectedItem = ContextMgr.SelectedGroup.Name;

            if (_edit)
            {
                _textTitle.Text = ContextMgr.SelectedKeys[0].Title;
                _textUsername.Text = ContextMgr.SelectedKeys[0].Username;
                _textPassword.Text = ContextMgr.SelectedKeys[0].Password;
                _textPassword2.Text = ContextMgr.SelectedKeys[0].Password;
                _textUrl.Text = ContextMgr.SelectedKeys[0].Url;
                _textNotes.Text = ContextMgr.SelectedKeys[0].Notes;
            }
        }

        

        #region Button Commands

        private void OnTest(object sender, EventArgs e)
        {
            _textTitle.Text = Randomizer.GetKeyTitle();
            _textUsername.Text = Randomizer.GetKeyUser();
            _textPassword.Text = Randomizer.GetKeyPassword();
            _textPassword2.Text = _textPassword.Text;
            string random = Randomizer.GetKeyURL();
            _textUrl.Text = "www.bu.edu/Page-" + random;
            _textNotes.Text = "Blah blah blah web site-" + random;
        }

        private void OnOk(object sender, EventArgs e)
        {
            if (_textTitle.Text == string.Empty)
            {
                MessageBox.Show(this, "Title cannot be blank", "My Key Pass");
            }
            else if (_textUsername.Text == string.Empty)
            {
                MessageBox.Show(this, "User Name cannot be blank", "My Key Pass");
            }
            else if (_textPassword.Text == string.Empty)
            {
                MessageBox.Show(this, "Password cannot be blank", "My Key Pass");
            }
            else if (_textPassword.Text != _textPassword2.Text)
            {
                MessageBox.Show(this, "Passwords do not match", "My Key Pass");
            }
            else if (_textUrl.Text == string.Empty)
            {
                MessageBox.Show(this, "URL cannot be blank", "My Key Pass");
            }
            else if (_textNotes.Text == string.Empty)
            {
                MessageBox.Show(this, "Notes cannot be blank", "My Key Pass");
            }
            else
            {
                foreach (Group g in _groups)
                {
                    if (g.Name.Equals(_groupComboList.SelectedItem))
                        _group = g;
                }
                _key.Title = _textTitle.Text;
                _key.Username = _textUsername.Text;
                _key.Password = _textPassword.Text;
                _key.Url = _textUrl.Text;
                _key.Notes = _textNotes.Text;
                DialogResult = DialogResult.OK;
                Close();
            }
        }

        private void OnCancel(object sender, EventArgs e)
        {
            DialogResult = DialogResult.Cancel;
            Close();
        }

        #endregion

        
    }
}
