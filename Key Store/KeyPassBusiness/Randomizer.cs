using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KeyPassBusiness
{
    public static class Randomizer
    {
        public static string GetGroupName()
        {
            Random rand = new Random();
            int randomNumber = rand.Next(300);
            return "Group-" + randomNumber.ToString();
        }

        public static string GetKeyTitle()
        {
            Random rand = new Random();
            int randomNumber = rand.Next(300);
            return "Title-" + randomNumber.ToString();
        }

        public static string GetKeyUser()
        {
            Random rand = new Random();
            int randomNumber = rand.Next(300);
            return "User-" + randomNumber.ToString();
        }

        public static string GetKeyPassword()
        {
            Random rand = new Random();
            int randomNumber = rand.Next(300);
            return "Password-" + randomNumber.ToString();
        }

        public static string GetKeyURL()
        {
            Random rand = new Random();
            int randomNumber = rand.Next(300);
            return randomNumber.ToString();
        }
        
    }
}
