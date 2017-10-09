namespace KeyPassUserInterface
{
    partial class ClockControl
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
            this.components = new System.ComponentModel.Container();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this._ClockLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // timer1
            // 
            this.timer1.Enabled = true;
            this.timer1.Interval = 1000;
            this.timer1.Tick += new System.EventHandler(this.OnClockTick);
            // 
            // _ClockLabel
            // 
            this._ClockLabel.AutoSize = true;
            this._ClockLabel.Dock = System.Windows.Forms.DockStyle.Fill;
            this._ClockLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this._ClockLabel.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this._ClockLabel.Location = new System.Drawing.Point(0, 0);
            this._ClockLabel.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this._ClockLabel.Name = "_ClockLabel";
            this._ClockLabel.Size = new System.Drawing.Size(0, 29);
            this._ClockLabel.TabIndex = 0;
            this._ClockLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this._ClockLabel.Click += new System.EventHandler(this.OnClockClick);
            // 
            // ClockControl
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(16F, 31F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.BackColor = System.Drawing.Color.SteelBlue;
            this.Controls.Add(this._ClockLabel);
            this.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.125F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Margin = new System.Windows.Forms.Padding(4);
            this.Name = "ClockControl";
            this.Size = new System.Drawing.Size(547, 47);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.Label _ClockLabel;
    }
}
