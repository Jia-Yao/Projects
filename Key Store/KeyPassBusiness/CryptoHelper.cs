using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace KeyPassBusiness
{
    public static class CryptoHelper
    {

        private static int _keySize = 256;

        public static byte[] Encrypt(byte[] plainBuffer)
        {
            byte[] cipherBuffer = null;
            byte[] cipherData = null;
            byte[] iv = new byte[16];
            byte[] key = new byte[32];

            AesManaged aes = new AesManaged();
            using (aes)
            {
                aes.KeySize = _keySize;
                aes.Mode = CipherMode.CBC;
                aes.Padding = PaddingMode.None;
                aes.GenerateIV();
                iv = aes.IV;
                aes.GenerateKey();
                key = aes.Key;

                MemoryStream ms = new MemoryStream();
                using (ms)
                {
                    CryptoStream cryptoStream = new CryptoStream(ms, aes.CreateEncryptor(key, iv), CryptoStreamMode.Write);
                    using (cryptoStream)
                    {
                        cryptoStream.Write(plainBuffer, 0, plainBuffer.Length);
                    }
                    cipherData = ms.GetBuffer();
                }
            }

            cipherBuffer = new byte[key.Length + iv.Length + cipherData.Length];
            Buffer.BlockCopy(key, 0, cipherBuffer, 0, key.Length);
            Buffer.BlockCopy(iv, 0, cipherBuffer, key.Length, iv.Length);
            Buffer.BlockCopy(cipherData, 0, cipherBuffer, key.Length + iv.Length, cipherData.Length);

            return cipherBuffer;
        }

        public static byte[] Decrypt(byte[] cipherBuffer)
        {
            byte[] plainBuffer = null;
            byte[] key = new byte[32];
            byte[] iv = new byte[16];
            byte[] cipherData = new byte[cipherBuffer.Length - 48];
            Buffer.BlockCopy(cipherBuffer, 0, key, 0, key.Length);
            Buffer.BlockCopy(cipherBuffer, key.Length, iv, 0, iv.Length);
            Buffer.BlockCopy(cipherBuffer, key.Length + iv.Length, cipherData, 0, cipherData.Length);

            AesManaged aes = new AesManaged();
            using (aes)
            {
                aes.KeySize = _keySize;
                aes.Mode = CipherMode.CBC;
                aes.Padding = PaddingMode.None;

                MemoryStream ms = new MemoryStream();
                using (ms)
                {
                    CryptoStream cryptoStream = new CryptoStream(ms, aes.CreateDecryptor(key, iv), CryptoStreamMode.Write);
                    using (cryptoStream)
                    {
                        cryptoStream.Write(cipherData, 0, cipherData.Length);
                    }
                    plainBuffer = ms.GetBuffer();
                }
            }

            return plainBuffer;
        }
    }
}
