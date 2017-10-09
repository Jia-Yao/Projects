namespace KeyPassUserInterface
{
    partial class GroupForm
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(GroupForm));
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this._buttonCancel = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this._textGroup = new System.Windows.Forms.TextBox();
            this._buttonTest = new System.Windows.Forms.Button();
            this._buttonOK = new System.Windows.Forms.Button();
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
            this.tableLayoutPanel1.Controls.Add(this._buttonCancel, 3, 1);
            this.tableLayoutPanel1.Controls.Add(this.label1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this._textGroup, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this._buttonTest, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this._buttonOK, 2, 1);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 2;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(775, 123);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // _buttonCancel
            // 
            this._buttonCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this._buttonCancel.Dock = System.Windows.Forms.DockStyle.Bottom;
            this._buttonCancel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._buttonCancel.Location = new System.Drawing.Point(635, 58);
            this._buttonCancel.Margin = new System.Windows.Forms.Padding(3, 3, 15, 15);
            this._buttonCancel.Name = "_buttonCancel";
            this._buttonCancel.Size = new System.Drawing.Size(125, 50);
            this._buttonCancel.TabIndex = 4;
            this._buttonCancel.Text = "Cancel";
            this._buttonCancel.UseVisualStyleBackColor = true;
            this._buttonCancel.Click += new System.EventHandler(this.OnCancel);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(3, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(176, 44);
            this.label1.TabIndex = 0;
            this.label1.Text = "Group Name:";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // _textGroup
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._textGroup, 3);
            this._textGroup.Dock = System.Windows.Forms.DockStyle.Fill;
            this._textGroup.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._textGroup.Location = new System.Drawing.Point(185, 3);
            this._textGroup.Name = "_textGroup";
            this._textGroup.Size = new System.Drawing.Size(587, 38);
            this._textGroup.TabIndex = 1;
            // 
            // _buttonTest
            // 
            this.tableLayoutPanel1.SetColumnSpan(this._buttonTest, 2);
            this._buttonTest.Dock = System.Windows.Forms.DockStyle.Bottom;
            this._buttonTest.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._buttonTest.Location = new System.Drawing.Point(15, 58);
            this._buttonTest.Margin = new System.Windows.Forms.Padding(15, 3, 3, 15);
            this._buttonTest.Name = "_buttonTest";
            this._buttonTest.Size = new System.Drawing.Size(459, 50);
            this._buttonTest.TabIndex = 2;
            this._buttonTest.Text = "Test";
            this._buttonTest.UseVisualStyleBackColor = true;
            this._buttonTest.Click += new System.EventHandler(this.OnTest);
            // 
            // _buttonOK
            // 
            this._buttonOK.Dock = System.Windows.Forms.DockStyle.Bottom;
            this._buttonOK.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._buttonOK.Location = new System.Drawing.Point(492, 58);
            this._buttonOK.Margin = new System.Windows.Forms.Padding(15, 3, 15, 15);
            this._buttonOK.Name = "_buttonOK";
            this._buttonOK.Size = new System.Drawing.Size(125, 50);
            this._buttonOK.TabIndex = 3;
            this._buttonOK.Text = "OK";
            this._buttonOK.UseVisualStyleBackColor = true;
            this._buttonOK.Click += new System.EventHandler(this.OnOk);
            // 
            // GroupForm
            // 
            this.AcceptButton = this._buttonOK;
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this._buttonCancel;
            this.ClientSize = new System.Drawing.Size(775, 123);
            this.Controls.Add(this.tableLayoutPanel1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "GroupForm";
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Group";
            this.Load += new System.EventHandler(this.OnLoad);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox _textGroup;
        private System.Windows.Forms.Button _buttonTest;
        private System.Windows.Forms.Button _buttonCancel;
        private System.Windows.Forms.Button _buttonOK;
    }
}