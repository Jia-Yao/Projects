using KeyPassInfoObjects;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KeyPassBusiness
{
    public static class ContextMgr
    {
        #region Non-Public Data Members

        private static Group _selectedGroup = null;
        private static List<Key> _selectedKeys = new List<Key>();

        #endregion

        public static void Initialize()
        {
            _selectedGroup = null;
            _selectedKeys = new List<Key>();
        }

        public static Group SelectedGroup
        {
            get { return _selectedGroup; }
            set { _selectedGroup = value; }
        }

        public static List<Key> SelectedKeys
        {
            get { return _selectedKeys; }
            set { _selectedKeys = value; }
        }
    }
}
