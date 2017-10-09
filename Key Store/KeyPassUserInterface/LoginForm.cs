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
    public partial class LoginForm : Form
    {

        public LoginForm()
        {
            InitializeComponent();
        }

        private bool ValidatePassword(string input)
        {
            if (input == "FooBarMeow")
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        private void OnOk(object sender, EventArgs e)
        {
            if (ValidatePassword(_textBox.Text))
            {
                DialogResult = DialogResult.OK;
                this.Close();
            }
            else
            {
                MessageBox.Show(this, "Invalid Password", "My Key Pass", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            
        }

        private void OnLoad(object sender, EventArgs e)
        {
            _textBox.Text = "FooBarMeow";
        }

        private void OnCheckChanged(object sender, EventArgs e)
        {
            if (_checkBox.Checked)
            {
                _textBox.PasswordChar = '\0';
            }
            else
            {
                _textBox.PasswordChar = '*';
            }
        }
    }
}
