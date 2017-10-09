namespace KeyPassUserInterface
{
    partial class KeyForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(KeyForm));
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this._buttonCancel = new System.Windows.Forms.Button();
            this._buttonOK = new System.Windows.Forms.Button();
            this._buttonTest = new System.Windows.Forms.Button();
            this._textNotes = new System.Windows.Forms.TextBox();
            this._textUrl = new System.Windows.Forms.TextBox();
            this._textPassword2 = new System.Windows.Forms.TextBox();
            this._textPassword = new System.Windows.Forms.TextBox();
            this._textUsername = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this._groupComboList = new System.Windows.Forms.ComboBox();
            this._textTitle = new System.Windows.Forms.TextBox();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 4;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.Controls.Add(this._buttonCancel, 3, 7);
            this.tableLayoutPanel1.Controls.Add(this._buttonOK, 2, 7);
            this.tableLayoutPanel1.Controls.Add(this._buttonTest, 0, 7);
            this.tableLayoutPanel1.Controls.Add(this._textNotes, 1, 6);
            this.tableLayoutPanel1.Controls.Add(this._textUrl, 1, 5);
            this.tableLayoutPanel1.Controls.Add(this._textPassword2, 1, 4);
            this.tableLayoutPanel1.Controls.Add(this._textPassword, 1, 3);
            this.tableLayoutPanel1.Controls.Add(this._textUsername, 1, 2);
            this.tableLayoutPanel1.Controls.Add(this.label1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.label2, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.label3, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.label4, 0, 3);
            this.tableLayoutPanel1.Controls.Add(this.label5, 0, 4);
            this.tableLayoutPanel1.Controls.Add(this.label6, 0, 5);
            this.tableLayoutPanel1.Controls.Add(this.label7, 0, 6);
            this.tableLayoutPanel1.Controls.Add(this._groupComboList, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this._textTitle, 1, 1);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 8;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.Size = new System.Drawing.Size(1038, 859);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // _buttonCancel
            // 
            this._buttonCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this._buttonCancel.Dock = System.Windows.Forms.DockStyle.Bottom;
            this._buttonCancel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._buttonCancel.Location = new System.Drawing.Point(898, 794);
            this._buttonCancel.Margin = new System.Windows.Forms.Padding(3, 15, 15, 15);
            this._buttonCancel.Name = "_buttonCancel";
            this._buttonCancel.Size = new System.Drawing.Size(125, 50);
            this._buttonCancel.TabIndex = 16;
            this._buttonCancel.Text = "Cancel";
            this._buttonCancel.UseVisualStyleBackColor = true;
            this._buttonCancel.Click += new System.EventHandler(this.OnCancel);
            // 
            // _buttonOK
            // 
            this._buttonOK.Dock = System.Windows.Forms.DockStyle.Bottom;
            this._buttonOK.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._buttonOK.Location = new System.Drawing.Point(755, 794);
            this._buttonOK.Margin = new System.Windows.Forms.Padding(15);
            this._buttonOK.Name = "_buttonOK";
            this._buttonOK.Size = new System.Drawing.Size(125, 50);
            this._buttonOK.TabIndex = 15;
            this._buttonOK.Text = "OK";
            this._buttonOK.UseVisualStyleBackColor = true;
            this._buttonOK.Click += new System.EventHandler(this.OnOk);
            // 
            // _buttonTest
            // 
            this._buttonTest.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.tableLayoutPanel1.SetColumnSpan(this._buttonTest, 2);
            this._buttonTest.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._buttonTest.Location = new System.Drawing.Point(15, 794);
            this._buttonTest.Margin = new System.Windows.Forms.Padding(15, 15, 3, 15);
            this._buttonTest.Name = "_buttonTest";
            this._buttonTest.Size = new System.Drawing.Size(326, 50);
            this._buttonTest.TabIndex = 14;
            this._buttonTest.Text = "Test";
            this._buttonTest.UseVisualStyleBackColor = true;
            this._buttonTest.Click += new System.EventHandler(this.OnTest);
            // 
            // _textNotes
            // 
            this._textNotes.BackColor = System.Drawing.Color.OldLace;
            this.tableLayoutPanel1.SetColumnSpan(this._textNotes, 3);
            this._textNotes.Dock = System.Windows.Forms.DockStyle.Fill;
            this._textNotes.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._textNotes.Location = new System.Drawing.Point(253, 317);
            this._textNotes.Margin = new System.Windows.Forms.Padding(3, 10, 3, 3);
            this._textNotes.Multiline = true;
            this._textNotes.Name = "_textNotes";
            this._textNotes.Size = new System.Drawing.Size(782, 459);
            this._textNotes.TabIndex = 13;
            // 
            // _textUrl
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._textUrl, 3);
            this._textUrl.Dock = System.Windows.Forms.DockStyle.Fill;
            this._textUrl.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._textUrl.Location = new System.Drawing.Point(253, 266);
            this._textUrl.Margin = new System.Windows.Forms.Padding(3, 10, 3, 3);
            this._textUrl.Name = "_textUrl";
            this._textUrl.Size = new System.Drawing.Size(782, 38);
            this._textUrl.TabIndex = 12;
            // 
            // _textPassword2
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._textPassword2, 3);
            this._textPassword2.Dock = System.Windows.Forms.DockStyle.Fill;
            this._textPassword2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._textPassword2.Location = new System.Drawing.Point(253, 215);
            this._textPassword2.Margin = new System.Windows.Forms.Padding(3, 10, 3, 3);
            this._textPassword2.Name = "_textPassword2";
            this._textPassword2.PasswordChar = '*';
            this._textPassword2.Size = new System.Drawing.Size(782, 38);
            this._textPassword2.TabIndex = 11;
            // 
            // _textPassword
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._textPassword, 3);
            this._textPassword.Dock = System.Windows.Forms.DockStyle.Fill;
            this._textPassword.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._textPassword.Location = new System.Drawing.Point(253, 164);
            this._textPassword.Margin = new System.Windows.Forms.Padding(3, 10, 3, 3);
            this._textPassword.Name = "_textPassword";
            this._textPassword.PasswordChar = '*';
            this._textPassword.Size = new System.Drawing.Size(782, 38);
            this._textPassword.TabIndex = 10;
            // 
            // _textUsername
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._textUsername, 3);
            this._textUsername.Dock = System.Windows.Forms.DockStyle.Fill;
            this._textUsername.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._textUsername.Location = new System.Drawing.Point(253, 113);
            this._textUsername.Margin = new System.Windows.Forms.Padding(3, 10, 3, 3);
            this._textUsername.Name = "_textUsername";
            this._textUsername.Size = new System.Drawing.Size(782, 38);
            this._textUsername.TabIndex = 9;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(3, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(244, 52);
            this.label1.TabIndex = 0;
            this.label1.Text = "Group:";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(3, 52);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(244, 51);
            this.label2.TabIndex = 1;
            this.label2.Text = "Title:";
            this.label2.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(3, 103);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(244, 51);
            this.label3.TabIndex = 2;
            this.label3.Text = "User Name:";
            this.label3.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label4.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label4.Location = new System.Drawing.Point(3, 154);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(244, 51);
            this.label4.TabIndex = 3;
            this.label4.Text = "Password:";
            this.label4.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label5.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label5.Location = new System.Drawing.Point(3, 205);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(244, 51);
            this.label5.TabIndex = 4;
            this.label5.Text = "Confirm Password:";
            this.label5.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label6.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label6.Location = new System.Drawing.Point(3, 256);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(244, 51);
            this.label6.TabIndex = 5;
            this.label6.Text = "URL:";
            this.label6.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label7.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label7.Location = new System.Drawing.Point(3, 307);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(244, 472);
            this.label7.TabIndex = 6;
            this.label7.Text = "Notes:";
            this.label7.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // _groupComboList
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._groupComboList, 3);
            this._groupComboList.Dock = System.Windows.Forms.DockStyle.Fill;
            this._groupComboList.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this._groupComboList.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._groupComboList.FormattingEnabled = true;
            this._groupComboList.Location = new System.Drawing.Point(253, 10);
            this._groupComboList.Margin = new System.Windows.Forms.Padding(3, 10, 3, 3);
            this._groupComboList.Name = "_groupComboList";
            this._groupComboList.Size = new System.Drawing.Size(782, 39);
            this._groupComboList.TabIndex = 7;
            // 
            // _textTitle
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._textTitle, 3);
            this._textTitle.Dock = System.Windows.Forms.DockStyle.Fill;
            this._textTitle.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._textTitle.Location = new System.Drawing.Point(253, 62);
            this._textTitle.Margin = new System.Windows.Forms.Padding(3, 10, 3, 3);
            this._textTitle.Name = "_textTitle";
            this._textTitle.Size = new System.Drawing.Size(782, 38);
            this._textTitle.TabIndex = 8;
            // 
            // KeyForm
            // 
            this.AcceptButton = this._buttonOK;
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this._buttonCancel;
            this.ClientSize = new System.Drawing.Size(1038, 859);
            this.Controls.Add(this.tableLayoutPanel1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "KeyForm";
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Key Properties";
            this.Load += new System.EventHandler(this.OnLoad);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.TextBox _textNotes;
        private System.Windows.Forms.TextBox _textUrl;
        private System.Windows.Forms.TextBox _textPassword2;
        private System.Windows.Forms.TextBox _textPassword;
        private System.Windows.Forms.TextBox _textUsername;
        private System.Windows.Forms.ComboBox _groupComboList;
        private System.Windows.Forms.TextBox _textTitle;
        private System.Windows.Forms.Button _buttonTest;
        private System.Windows.Forms.Button _buttonOK;
        private System.Windows.Forms.Button _buttonCancel;
    }
}