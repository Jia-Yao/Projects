using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using KeyPassBusiness;

namespace KeyPassUserInterface
{
    public partial class StatusControl : UserControl
    {
        public StatusControl()
        {
            InitializeComponent();
        }

        private void OnLoad(object sender, EventArgs e)
        {
            Application.Idle += OnIdle;
        }

        private void OnIdle(object sender, EventArgs e)
        {
            int numGroups = KeyPassMgr.GetAllGroups().Count;
            int numKeys = (ContextMgr.SelectedGroup == null) ? 0 : ContextMgr.SelectedGroup.Keys.Count;
            int numSelectedKeys = ContextMgr.SelectedKeys.Count;

            _groupsLabel.Text = "Groups: " + numGroups.ToString();
            _keysLabel.Text = numSelectedKeys.ToString() + " selected keys of " + numKeys.ToString();


        }
        
        public void AddAuditTrail(string record)
        {
            _auditTrailList.Items.Add(record);
            _auditTrailList.SelectedItem = record;
        }
    }
}
