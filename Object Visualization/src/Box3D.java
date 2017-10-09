//****************************************************************************
//      Box class
//****************************************************************************
// History :
//   Dec 6, 2015 Created by Jia Yao
//

public class Box3D
{
	private Vector3D center, tmp = new Vector3D();
	public float height;
	private int step;
	public Mesh3D mesh;
	private Quaternion Q = new Quaternion();
	
	public Box3D(float _x, float _y, float _z, float _h, int _step)
	{
		center = new Vector3D(_x,_y,_z);
		height = _h;
		step = _step;
		initMesh();
	}
	
	public void set_center(float _x, float _y, float _z)
	{
		center.x=_x;
		center.y=_y;
		center.z=_z;
		fillMesh();  // update the triangle mesh
	}
	
	public void set_height(float _h)
	{
		height = _h;
		fillMesh(); // update the triangle mesh
	}
	
	public void set_step(int _step)
	{
		step = _step;
		initMesh(); // resized the mesh, must re-initialize
	}
	
	public int get_step()
	{
		return step;
	}

	private void initMesh()
	{
		mesh = new Mesh3D(6*step,step);		// for the 6 faces
		fillMesh();  // set the mesh vertices and normals
	}
	
	public void translate (float _xOffset, float _yOffset, float _zOffset){
		center.x += _xOffset;
		center.y += _yOffset;
		center.z += _zOffset;
	}
	
	public void rotate(Quaternion p){
		Q = p.multiply(Q);
		Q.normalize();
	}
	
	public void reset(){
		fillMesh();
		tmp.set(center.x+(float)height/2, center.y+(float)height/2, center.z+(float)height/2);
		mesh.rotateMesh(Q, tmp);
	}
	
	// fill the triangle mesh vertices and normals
	private void fillMesh()
	{
		int i,j;		
		float v,u;
		float d_v= height / ((float)(step-1));
		float d_u= height / ((float)(step-1));
		
		for(i=0,v=0; i<step; ++i,v += d_v)
	    {	
			for(j=0,u=0; j<step; ++j,u += d_u)
			{
				// vertex location (for each of the six faces)
				mesh.v[i][j].x=center.x+v;
				mesh.v[i][j].y=center.y+u;
				mesh.v[i][j].z=center.z;
				mesh.v[i+step][j].x=center.x+v;
				mesh.v[i+step][j].y=center.y+u;
				mesh.v[i+step][j].z=center.z+height;
				mesh.v[i+2*step][j].x=center.x;
				mesh.v[i+2*step][j].y=center.y+v;
				mesh.v[i+2*step][j].z=center.z+u;
				mesh.v[i+3*step][j].x=center.x+height;
				mesh.v[i+3*step][j].y=center.y+v;
				mesh.v[i+3*step][j].z=center.z+u;
				mesh.v[i+4*step][j].x=center.x+v;
				mesh.v[i+4*step][j].y=center.y;
				mesh.v[i+4*step][j].z=center.z+u;
				mesh.v[i+5*step][j].x=center.x+v;
				mesh.v[i+5*step][j].y=center.y+height;
				mesh.v[i+5*step][j].z=center.z+u;
				
				// unit normal to cylinder at this vertex (for each of the six faces)
				mesh.n[i][j].x = 0;
				mesh.n[i][j].y = 0;
				mesh.n[i][j].z = -1;
				mesh.n[i+step][j].x = 0;
				mesh.n[i+step][j].y = 0;
				mesh.n[i+step][j].z = 1;
				mesh.n[i+2*step][j].x = -1;
				mesh.n[i+2*step][j].y = 0;
				mesh.n[i+2*step][j].z = 0;
				mesh.n[i+3*step][j].x = 1;
				mesh.n[i+3*step][j].y = 0;
				mesh.n[i+3*step][j].z = 0;
				mesh.n[i+4*step][j].x = 0;
				mesh.n[i+4*step][j].y = -1;
				mesh.n[i+4*step][j].z = 0;
				mesh.n[i+5*step][j].x = 0;
				mesh.n[i+5*step][j].y = 1;
				mesh.n[i+5*step][j].z = 0;
				
			}
	    }
	}
}