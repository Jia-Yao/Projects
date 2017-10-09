using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KeyPassInfoObjects
{
    [Serializable]
    public class Group
    {
        private string _name = string.Empty;
        private List<Key> _keys = new List<Key>();

        public string Name
        {
            get { return _name; }
            set { _name = value; }
        }

        public List<Key> Keys
        {
            get { return _keys; }
            set { _keys = value; }
        }
        


    }
}
