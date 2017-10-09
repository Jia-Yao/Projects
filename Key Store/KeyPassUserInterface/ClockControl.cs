using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Diagnostics;

namespace KeyPassUserInterface
{
    public partial class ClockControl : UserControl
    {
        public ClockControl()
        {
            InitializeComponent();
        }

        private void OnClockTick(object sender, EventArgs e)
        {
            _ClockLabel.Text = DateTime.Now.ToString();
        }

        private void OnClockClick(object sender, EventArgs e)
        {
            Process.Start("timedate.cpl");
        }
    }
}
