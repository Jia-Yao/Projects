using KeyPassInfoObjects;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace KeyPassBusiness
{
    public static class KeyPassMgr
    {
        
        private static KeyPassDocument _document = new KeyPassDocument();

        public static event DocumentLoadedEventHandler DocumentLoaded;

        public static event DocumentLoadedEventHandler DocumentSaved;

        public static KeyPassDocument Document
        {
            get { return _document; }
        }

        public static void New()
        {
            _document = new KeyPassDocument();

            if (DocumentLoaded != null)
            {
                DocumentLoaded.Invoke(_document);
            }
        }

        public static void Open(string filename)
        {
            _document = new KeyPassDocument();

            /*
            // open unencrypted XML file
            XmlTextReader xmlReader = new XmlTextReader(filename);
            Group g = new Group();
            Key k = new Key();
            using (xmlReader)
            {

                while (xmlReader.Read()) // Read nodes sequentially
                {
                    if (xmlReader.NodeType == XmlNodeType.Element)
                    {
                        if (xmlReader.Name == "Group")
                        {
                            g = new Group();
                            g.Name = xmlReader.GetAttribute("Name");

                            _document.Groups.Add(g);
                        }
                        else if (xmlReader.Name == "Key")
                        {
                            k = new Key();
                            k.Title = xmlReader.GetAttribute("Title");
                            k.Username = xmlReader.GetAttribute("Username");
                            k.Password = xmlReader.GetAttribute("Password");
                            k.Url = xmlReader.GetAttribute("URL");
                            k.Notes = xmlReader.GetAttribute("Notes");

                            KeyPassMgr.AddKeyToGroup(g, k);
                        }
                    }
                }
            }
            */

            byte[] cipherBuffer = File.ReadAllBytes(filename);
            byte[] plainBuffer = CryptoHelper.Decrypt(cipherBuffer);
            MemoryStream ms = new MemoryStream(plainBuffer);
            using (ms)
            {
                BinaryFormatter bf = new BinaryFormatter();
                bf.AssemblyFormat = System.Runtime.Serialization.Formatters.FormatterAssemblyStyle.Simple;
                _document.Groups = (List<Group>)bf.Deserialize(ms);
            }

            _document.Filename = filename;
            _document.IsModified = false;

            if (DocumentLoaded != null)
            {
                DocumentLoaded.Invoke(_document);
            }
        }

        public static void Save()
        {
            SaveAs(_document.Filename);
        }

        public static void SaveAs(string filename)
        {
            /* 
            // The unencrypted way to save
            XmlTextWriter xmlWriter = new XmlTextWriter(filename, null);

            using (xmlWriter)
            {
                xmlWriter.WriteStartDocument();
                xmlWriter.WriteStartElement("", "MyKeyPass", "");

                foreach (Group g in _document.Groups)
                {
                    xmlWriter.WriteStartElement("", "Group", "");
                    xmlWriter.WriteAttributeString("Name", g.Name);
                    xmlWriter.WriteEndElement();
                    foreach (Key k in g.Keys)
                    {
                        xmlWriter.WriteStartElement("", "Key", "");
                        xmlWriter.WriteAttributeString("Title", k.Title);
                        xmlWriter.WriteAttributeString("Username", k.Username);
                        xmlWriter.WriteAttributeString("Password", k.Password);
                        xmlWriter.WriteAttributeString("URL", k.Url);
                        xmlWriter.WriteAttributeString("Notes", k.Notes);
                        xmlWriter.WriteEndElement();
                    }
                }

                xmlWriter.WriteEndElement();

                xmlWriter.Flush();
                
            }
            */

            MemoryStream ms = new MemoryStream();
            using (ms)
            {
                BinaryFormatter bf = new BinaryFormatter();
                bf.AssemblyFormat = System.Runtime.Serialization.Formatters.FormatterAssemblyStyle.Simple;
                bf.Serialize(ms, _document.Groups);

                byte[] plainBuffer = ms.GetBuffer();
                
                byte[] cipherBuffer = CryptoHelper.Encrypt(plainBuffer);

                File.WriteAllBytes(filename, cipherBuffer);

            }

            _document.Filename = filename;
            _document.IsModified = false;

            if (DocumentSaved != null)
            {
                DocumentSaved.Invoke(_document);
            }
        }

        public static void AddGroup(Group group)
        {
            _document.Groups.Add(group);
            _document.IsModified = true;
        }

        public static void EditGroup(Group group, string newName)
        {
            group.Name = newName;
            _document.IsModified = true;
        }

        public static Group DuplicateGroup(Group group)
        {
            Group newGroup = new Group();
            newGroup.Name = group.Name + "-" + DateTime.Now.ToString();
            Key newKey;
            foreach (Key key in group.Keys)
            {
                newKey = new Key();
                newKey.Title = key.Title;
                newKey.Username = key.Username;
                newKey.Password = key.Password;
                newKey.Url = key.Url;
                newKey.Notes = key.Notes;
                newGroup.Keys.Add(newKey);
            }
            _document.Groups.Add(newGroup);
            _document.IsModified = true;
            return newGroup;
        }

        public static void DeleteGroup(Group group)
        {
            _document.Groups.Remove(group);
            _document.IsModified = true;
        }

        public static List<Group> GetAllGroups()
        {
            return _document.Groups;
        }

        public static void AddKeyToGroup(Group group, Key key)
        {
            group.Keys.Add(key);
            _document.IsModified = true;
        }

        public static void EditKey(Key originalKey, Key editedKey)
        {
            originalKey.Title = editedKey.Title;
            originalKey.Username = editedKey.Username;
            originalKey.Password = editedKey.Password;
            originalKey.Url = editedKey.Url;
            originalKey.Notes = editedKey.Notes;
            _document.IsModified = true;
        }

        public static Key DuplicateKey(Key key)
        {
            Key newKey = new Key();
            newKey = new Key();
            newKey.Title = key.Title;
            newKey.Username = key.Username;
            newKey.Password = key.Password;
            newKey.Url = key.Url;
            newKey.Notes = key.Notes;
            ContextMgr.SelectedGroup.Keys.Add(newKey);
            _document.IsModified = true;
            return newKey;
        }

        public static void DeleteKeyFromGroup(Group group, Key key)
        {
            group.Keys.Remove(key);
            _document.IsModified = true;
        }
        
    }
}
