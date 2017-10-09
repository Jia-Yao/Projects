using KeyPassUserInterface;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace KeyPassApp
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            LoginForm f = new LoginForm();
            if (f.ShowDialog() != DialogResult.OK)
                return;
            Application.Run(new MainForm());
        }
    }
}
