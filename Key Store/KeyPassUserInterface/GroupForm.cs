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
    public partial class GroupForm : Form
    {

        private Group _group = new Group();
        private bool _edit = false;

        public Group Group
        {
            get { return _group; }
            set { _group = value; }
        }

        public bool Edit
        {
            get { return _edit; }
            set { _edit = value; }
        }

        public GroupForm()
        {
            InitializeComponent();
        }

        private void OnLoad(object sender, EventArgs e)
        {
            if (_edit)
            {
                _textGroup.Text = ContextMgr.SelectedGroup.Name;
            }
        }

        #region Button Commands

        private void OnTest(object sender, EventArgs e)
        {
            _textGroup.Text = Randomizer.GetGroupName();
        }

        private void OnOk(object sender, EventArgs e)
        {
            if (_textGroup.Text == string.Empty)
            {
                MessageBox.Show(this, "Group Name cannot be blank", "My Key Pass");
            }
            else
            {
                _group.Name = _textGroup.Text;
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
