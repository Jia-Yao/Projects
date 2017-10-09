using KeyPassBusiness;
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
    public partial class AboutForm : Form
    {
        public AboutForm()
        {
            InitializeComponent();
        }

        private void OnOK(object sender, EventArgs e)
        {
            Close();
        }

        private void OnLoad(object sender, EventArgs e)
        {
            _creatorLabel.Text = MyCompany.ProductName + "\n\n" +
                MyCompany.ProductVersion + "\n\n" +
                MyCompany.ProductAuthor + "\n\n" +
                MyCompany.Name;
            _descriptionText.Text = MyCompany.ProductDescription;
        }
    }
}
