//****************************************************************************
//      DepthBuffer class
//****************************************************************************
// History :
//   Nov 30, 2015 Created by Jia Yao
//

public class DepthBuffer {
	public int[][] mat;
	public int height, width;
	
	public DepthBuffer(int width, int height){
		mat = new int[width][height];
		this.height = height;
		this.width = width;
	}
}
