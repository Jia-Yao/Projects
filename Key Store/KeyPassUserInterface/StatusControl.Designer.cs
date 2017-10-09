namespace KeyPassUserInterface
{
    partial class StatusControl
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

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this._auditTrailList = new System.Windows.Forms.ComboBox();
            this._keysLabel = new System.Windows.Forms.Label();
            this._groupsLabel = new System.Windows.Forms.Label();
            this.clockControl1 = new KeyPassUserInterface.ClockControl();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 4;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.Controls.Add(this._auditTrailList, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this._keysLabel, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this._groupsLabel, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.clockControl1, 3, 0);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 1;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(1183, 36);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // _auditTrailList
            // 
            this._auditTrailList.Dock = System.Windows.Forms.DockStyle.Fill;
            this._auditTrailList.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this._auditTrailList.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._auditTrailList.FormattingEnabled = true;
            this._auditTrailList.Location = new System.Drawing.Point(3, 3);
            this._auditTrailList.Name = "_auditTrailList";
            this._auditTrailList.Size = new System.Drawing.Size(527, 40);
            this._auditTrailList.TabIndex = 0;
            // 
            // _keysLabel
            // 
            this._keysLabel.AutoSize = true;
            this._keysLabel.Dock = System.Windows.Forms.DockStyle.Fill;
            this._keysLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._keysLabel.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this._keysLabel.Location = new System.Drawing.Point(675, 0);
            this._keysLabel.Name = "_keysLabel";
            this._keysLabel.Size = new System.Drawing.Size(254, 36);
            this._keysLabel.TabIndex = 2;
            this._keysLabel.Text = "0 selected keys of 0";
            this._keysLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // _groupsLabel
            // 
            this._groupsLabel.AutoSize = true;
            this._groupsLabel.Dock = System.Windows.Forms.DockStyle.Fill;
            this._groupsLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._groupsLabel.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this._groupsLabel.Location = new System.Drawing.Point(536, 0);
            this._groupsLabel.Name = "_groupsLabel";
            this._groupsLabel.Size = new System.Drawing.Size(133, 36);
            this._groupsLabel.TabIndex = 1;
            this._groupsLabel.Text = "Groups: 0";
            this._groupsLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // clockControl1
            // 
            this.clockControl1.AutoSize = true;
            this.clockControl1.BackColor = System.Drawing.Color.SteelBlue;
            this.clockControl1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.clockControl1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.clockControl1.Location = new System.Drawing.Point(936, 4);
            this.clockControl1.Margin = new System.Windows.Forms.Padding(4);
            this.clockControl1.Name = "clockControl1";
            this.clockControl1.Size = new System.Drawing.Size(243, 28);
            this.clockControl1.TabIndex = 3;
            // 
            // StatusControl
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
            this.BackColor = System.Drawing.Color.SteelBlue;
            this.Controls.Add(this.tableLayoutPanel1);
            this.Name = "StatusControl";
            this.Size = new System.Drawing.Size(1183, 36);
            this.Load += new System.EventHandler(this.OnLoad);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.ComboBox _auditTrailList;
        private System.Windows.Forms.Label _keysLabel;
        private System.Windows.Forms.Label _groupsLabel;
        private ClockControl clockControl1;
    }
}
