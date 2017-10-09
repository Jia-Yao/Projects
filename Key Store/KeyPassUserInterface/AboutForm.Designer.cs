namespace KeyPassUserInterface
{
    partial class AboutForm
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(AboutForm));
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this._creatorLabel = new System.Windows.Forms.Label();
            this._descriptionText = new System.Windows.Forms.TextBox();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this._ButtonOK = new System.Windows.Forms.Button();
            this.tableLayoutPanel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            this.SuspendLayout();
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 35F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 65F));
            this.tableLayoutPanel1.Controls.Add(this._creatorLabel, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this._descriptionText, 1, 1);
            this.tableLayoutPanel1.Controls.Add(this.pictureBox1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this._ButtonOK, 1, 2);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 3;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 45F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 40F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 15F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(524, 279);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // _creatorLabel
            // 
            this._creatorLabel.AutoSize = true;
            this._creatorLabel.Dock = System.Windows.Forms.DockStyle.Fill;
            this._creatorLabel.Location = new System.Drawing.Point(198, 0);
            this._creatorLabel.Margin = new System.Windows.Forms.Padding(15, 0, 5, 0);
            this._creatorLabel.Name = "_creatorLabel";
            this._creatorLabel.Size = new System.Drawing.Size(321, 125);
            this._creatorLabel.TabIndex = 1;
            this._creatorLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // _descriptionText
            // 
            this._descriptionText.Dock = System.Windows.Forms.DockStyle.Fill;
            this._descriptionText.Location = new System.Drawing.Point(198, 128);
            this._descriptionText.Margin = new System.Windows.Forms.Padding(15, 3, 15, 3);
            this._descriptionText.Multiline = true;
            this._descriptionText.Name = "_descriptionText";
            this._descriptionText.ReadOnly = true;
            this._descriptionText.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this._descriptionText.Size = new System.Drawing.Size(311, 105);
            this._descriptionText.TabIndex = 2;
            this._descriptionText.TabStop = false;
            this._descriptionText.Text = "\r\n";
            // 
            // pictureBox1
            // 
            this.pictureBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.pictureBox1.Image = global::KeyPassUserInterface.Properties.Resources.Gradient1;
            this.pictureBox1.Location = new System.Drawing.Point(15, 15);
            this.pictureBox1.Margin = new System.Windows.Forms.Padding(15, 15, 3, 15);
            this.pictureBox1.Name = "pictureBox1";
            this.tableLayoutPanel1.SetRowSpan(this.pictureBox1, 3);
            this.pictureBox1.Size = new System.Drawing.Size(165, 249);
            this.pictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.pictureBox1.TabIndex = 0;
            this.pictureBox1.TabStop = false;
            // 
            // _ButtonOK
            // 
            this._ButtonOK.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this._ButtonOK.Location = new System.Drawing.Point(417, 246);
            this._ButtonOK.Margin = new System.Windows.Forms.Padding(3, 10, 15, 10);
            this._ButtonOK.Name = "_ButtonOK";
            this._ButtonOK.Size = new System.Drawing.Size(92, 23);
            this._ButtonOK.TabIndex = 3;
            this._ButtonOK.Text = "OK";
            this._ButtonOK.UseVisualStyleBackColor = true;
            this._ButtonOK.Click += new System.EventHandler(this.OnOK);
            // 
            // AboutForm
            // 
            this.AcceptButton = this._ButtonOK;
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
            this.ClientSize = new System.Drawing.Size(524, 279);
            this.Controls.Add(this.tableLayoutPanel1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "AboutForm";
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "My Key Pass";
            this.Load += new System.EventHandler(this.OnLoad);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.Label _creatorLabel;
        private System.Windows.Forms.TextBox _descriptionText;
        private System.Windows.Forms.Button _ButtonOK;
    }
}