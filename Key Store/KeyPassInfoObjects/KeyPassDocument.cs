using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KeyPassInfoObjects
{
    [Serializable]
    public class KeyPassDocument
    {
        #region Non-Public Data Members

        private List<Group> _groups = new List<Group>();
        private bool _isModified = false;
        private string _filename = String.Empty;

        #endregion

        #region Public Interface

        public List<Group> Groups
        {
            get { return _groups; }
            set { _groups = value; }
        }

        public string Filename
        {
            get { return _filename; }
            set { _filename = value; }
        }

        public bool IsModified
        {
            get { return _isModified; }
            set { _isModified = value; }
        }

        public bool IsNamed
        {
            get { return !string.IsNullOrEmpty(_filename); }
        }

        public bool IsEmpty
        {
            get { return _groups.Count == 0; }
        }
        #endregion
    }
}
