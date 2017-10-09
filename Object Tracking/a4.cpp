/*
a4.cpp
Author: Justin Chen, Jia Yao, Mohammed Hashem, Tania Papandrea
Team: Justin Chen, Jia Yao, Mohammed Hashem, Tania Papandrea
Date: 10/25/15
CS585 Image and Video Computing Fall 2015
Assignment 4 part 2 Basic Tracking
--------------
This program:
Takes video images from the Bats and Aquarium data set as input,
1) uses a greedy algorithm to do data association between the points in successive frames
2) displays the drawn tracks in a window
--------------
*/

#include "opencv2/core/core.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/video/tracking.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace cv;
using namespace std;

/**
Reads the label maps contained in the text file and converts the label maps
into a labelled matrix, as well as a binary image containing the segmented objects

@param filename - The filename string
@param labelled - The matrix that will contain the object label information
Background is labelled 0, and object labels start at 1
@param binary - The matrix that will contain the segmented objects that can be displayed
Background is labelled 0, and objects are labelled 255
*/
void convertFileToMat(String filename, Mat& labelled, Mat& binary);

/**
Reads the x-y object detections from the text file and draws in on a black and white image

@param filename - The filename string
@param binary3channel - The 3 channel image corresponding to the 1 channel binary image outputted
by convertFileToMat - The object detection positions will be drawn on this image
*/
void drawObjectDetections(String filename, Mat& binary3channel, vector <vector <float> > &data);

//void filter(vector <KalmanFilter> kals, Mat_<float> measurement, Mat_<float> state, vector <vector <float> > data, vector<float> predictPt, int imageNum);

/*
@param dir - directory name
@param imageNum - image number
@param extension - file extension
*/
string getImagePath(string dir, int imageNum, string extension);

/* Function that segements out the fish using the red borders
@param src - the source fish image in RGB
@param dst1 - the segmented image (fish in white and background in black)
@param dst2 - the segmented image with centroids marked as red circles
@param data - the centroids
*/
void fishsegmentation(Mat& src, Mat& dst1, Mat& dst2, vector <vector <float> > &data);

//vector< vector<float> > objects, kalman;

int main()
{
	int dataset;

	cout << "Datasets:\n1. Bats\n2. Aquarium\n";//\n2. Aquarium\n";
	cout << "Choose a dataset.\nSelect by entering the menu item number: ";
	cin >> dataset;

	int imageNum = 1;

	namedWindow("Detections", WINDOW_AUTOSIZE);

	vector < vector < vector <float> > > tracks;    // stores all the tracks (each track is a vector of points, each point is a vector of floats)

	/***
	Initialize Kalman filter
	***/
	//vector <KalmanFilter> kals;
	//KalmanFilter KF(4, 2, 0);
	//Mat_<float> state(4, 1); /* (x, y, Vx, Vy) */
	//Mat processNoise(4, 1, CV_32F);
	//Mat_<float> measurement(2,1);
	//measurement.setTo(Scalar(0));
	//vector<float> predictPt;
	//vector<int> markers;

	vector < vector <int> > colors;     // stores randomly generated colors (each color is a vector of RGB values)
	vector <int> color;
	for (int i = 0; i<70; i++){
		color.clear();
		color.push_back(rand() % 255);
		color.push_back(rand() % 255);
		color.push_back(rand() % 255);
		colors.push_back(color);
	}

	while (1)
	{
		Mat binary3C;
		vector <vector <float>> data;
		if (dataset == 1)
		{
			String filename_seg = getImagePath("bat/segmented", imageNum, "txt");
			Mat labelled, binary;
			convertFileToMat(filename_seg, labelled, binary);
			String filename_det = getImagePath("bat/localized", imageNum, "txt");
			cvtColor(binary, binary3C, CV_GRAY2BGR);
			drawObjectDetections(filename_det, binary3C, data);
		}
		else if (dataset == 2){
			String filename_seg = "aquarium/" + to_string(imageNum) + ".jpg";
			Mat original, original_hsv, segmented;
			original = imread(filename_seg, IMREAD_COLOR);
			cvtColor(original, original_hsv, CV_BGR2HSV);
			segmented = Mat::zeros(original.size(), CV_8UC1);
			binary3C = Mat::zeros(original.size(), CV_8UC3);
			fishsegmentation(original_hsv, segmented, binary3C, data);
		}
		//if we're at frame 0, initialize all tracks with the first points
		//tracks[i][0][0] = x value
		//tracks[i][0][1] = y value
		// i = track number
		vector < vector <float> > track;
		vector <float> point;
		if (imageNum == 1){
			for (int i = 0; i<data.size(); i++){
				track.clear();
				point.clear();
				point.push_back(data[i][0]);
				point.push_back(data[i][1]);
				track.push_back(point);
				tracks.push_back(track);
				Point center(data[i][0], data[i][1]);
				circle(binary3C, center, 3, Scalar(colors[i % 70][0], colors[i % 70][1], colors[i % 70][2]), -1, 8);
			}
		}
		else {
			//create prev vector that contains the last point in each of the tracks
			//prev[i][0]= x value
			//prev[i][1]= y value
			//prev[i][2]= number of points in the track
			//i = track number
			vector < vector <float> > prev;
			vector <float> entry;
			int i, j;
			for (i = 0; i<tracks.size(); i++){
				entry.clear();
				j = tracks[i].size();
				entry.push_back(tracks[i][j - 1][0]);
				entry.push_back(tracks[i][j - 1][1]);
				entry.push_back(j);
				prev.push_back(entry);
			}

			//create distance vector that contains distances between all points of prev and data
			//distances[i][2]= d
			//distances[i][0]= prev index / track number
			//distances[i][1]= data index of next point to be added to the track
			//i = entry number
			vector < vector <float> > distances;
			for (i = 0; i<prev.size(); i++){
				for (j = 0; j<data.size(); j++){
					int x1 = prev[i][0];
					int x2 = data[j][0];
					int y1 = prev[i][1];
					int y2 = data[j][1];
					double d;
					if ((x1 != -1) && (y2 != -1)){
						// prev index belongs to a terminated track
						d = (float)sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
						entry.clear();
						entry.push_back((float)i);
						entry.push_back((float)j);
						entry.push_back((float)d);
						distances.push_back(entry);
					}
				}
			}

			//sort by distance
			struct sorter {
				bool operator()(const vector<float> &left,
					const vector<float> &right) {
					return left.at(2) < right.at(2);
				}
			};
			sort(distances.begin(), distances.end(), sorter());

			// boolean array to tell which points have been associated
			vector<bool> prev_point_matched;
			vector<bool> data_point_matched;
			for (i = 0; i<prev.size(); i++){
				prev_point_matched.push_back(false);
			}
			for (i = 0; i<data.size(); i++){
				data_point_matched.push_back(false);
			}

			//iterate through distances vector from i=0 to i=len(distances)-1
			//add at most one point to each track
			vector <float> point;
			for (i = 0; i<distances.size(); i++){
				int track_number = distances[i][0];
				int prev_num_in_track = prev[track_number][2];
				// if number of points in track hasn't changed yet, add the new point
				if ((tracks[track_number].size()) == prev_num_in_track){
					//find the point to be added
					int data_index = distances[i][1];
					point.clear();
					point.push_back(data[data_index][0]);
					point.push_back(data[data_index][1]);
					// mark the indices of these points as already matched
					prev_point_matched[track_number] = true;
					data_point_matched[data_index] = true;
					// add the point to the track
					tracks[track_number].push_back(point);

				}
				// if the distances between points is already greater than threshold, stop matching points
				if (distances[i][2] > 55){
					break;
				}

			}

			// the existing tracks to terminate
			for (int i = 0; i<prev_point_matched.size(); i++){
				if (prev_point_matched[i] == false){
					// push (-1,-1) at the end of track to mark its termination
					point.clear();
					point.push_back(-1);
					point.push_back(-1);
					tracks[i].push_back(point);
				}
			}

			// the new tracks to add
			for (int i = 0; i<data_point_matched.size(); i++){
				if (data_point_matched[i] == false){
					track.clear();
					point.clear();
					point.push_back(data[i][0]);
					point.push_back(data[i][1]);
					track.push_back(point);
					tracks.push_back(track);
				}
			}

			// draw the point and a line segment to show the track
			// Documentation for drawing circle and line: http://docs.opencv.org/modules/core/doc/drawing_functions.html
			// for nicer visual effect, for each not terminated track, draw line segments using previous points
			int numTracks = tracks.size();
			int trackSize, x, y, counter, x1, x2, y1, y2;
			for (int i = 0; i < numTracks; i++){
				trackSize = tracks[i].size();
				x = tracks[i][trackSize - 1][0];
				y = tracks[i][trackSize - 1][1];
				if ((x != -1) && (y != -1)){    // if this track has not terminated
					Point center(x, y);
					circle(binary3C, center, 3, Scalar(colors[(i) % 70][0], colors[(i) % 70][1], colors[(i) % 70][2]), -1, 8);
					counter = trackSize;
					//cout << "track" << i << ":" << trackSize << endl;
					for (int j = 1; j < 10 && counter > 1; j++, counter--){
						x1 = tracks[i][trackSize - j][0];
						y1 = tracks[i][trackSize - j][1];
						x2 = tracks[i][trackSize - j - 1][0];
						y2 = tracks[i][trackSize - j - 1][1];
						line(binary3C, Point(x1, y1), Point(x2, y2), Scalar(colors[i % 70][0], colors[i % 70][1], colors[i % 70][2]), 2);
					}
				}
			}


		}

		imshow("Detections", binary3C);

		if (dataset == 1){
			if (imageNum == 151){
				imageNum = 1;
				/*for (int i=0; i<tracks.size(); i++){
				cout << "TRACK " << i << endl;
				for (int j=0; j<tracks[i].size(); j++){
				cout << tracks[i][j][0] << "," << tracks[i][j][1] << endl;
				}
				cout << "\n" << endl;
				}*/
				tracks.clear();

			}
			else{
				imageNum++;
			}
		}
		else if (dataset == 2){
			if (imageNum == 53){
				imageNum = 1;
				/*for (int i=0; i<tracks.size(); i++){
				cout << "TRACK " << i << endl;
				for (int j=0; j<tracks[i].size(); j++){
				cout << tracks[i][j][0] << "," << tracks[i][j][1] << endl;
				}
				cout << "\n" << endl;
				}*/
				tracks.clear();

			}
			else{
				imageNum++;
			}
		}

		if (waitKey(30) == 27)
		{
			cout << "esc key is pressed by user" << endl;
			break;
		}

	}

	return 0;
}

/*
void filter(vector <KalmanFilter> kals, Mat_<float> measurement, Mat_<float> state, vector <vector <float> > data, vector< vector<float> > predictPt, int imageNum)
{
//iterate for each object
for(int i = 0; i < data.size(); i++)
{
if(imageNum == 1)
{
//set initial points
KF.statePre.at<float>(0) = data.at(i).at(0);
KF.statePre.at<float>(1) = data.at(i).at(1);
KF.statePre.at<float>(2) = 0;
KF.statePre.at<float>(3) = 0;

//transition matrix
KF.transitionMatrix = Mat::eye(4, 4, CV_32F);

//set Kalman filter
setIdentity(KF.measurementMatrix);
setIdentity(KF.processNoiseCov, Scalar::all(1e-4));
setIdentity(KF.measurementNoiseCov, Scalar::all(1e-1));
setIdentity(KF.errorCovPost, Scalar::all(.1));
}
else
{
Mat prediction = KF.predict();
predictPt.push_back(prediction);

measurement(0) = data.at(i).at(0);
measurement(1) = data.at(i).at(1);

vector<float> measPt;
measPt.push_back(measurement(0));
measPt.push_back(measurement(1));
objects.push_back(measPt);
// generate measurementexit

//measurement += KF.measurementMatrix*state;

Mat estimated = KF.correct(measurement);
vector<float> statePt;
statePt.push_back(estimated.at<float>(0));
statePt.push_back(estimated.at<float>(1));
kalman.push_back(statePt);
}
}
}
*/

void fishsegmentation(Mat& src, Mat& dst1, Mat& dst2, vector <vector <float> > &data) {
	Mat borders = Mat::zeros(src.size(), CV_8UC1);
	// constants for selecting red colors
	const int HUE_MIN = 245;
	const int HUE_MAX = 10;
	const int SATURATION_MIN = 50;
	const int SATURATION_MAX = 255;
	const int VALUE_MIN = 50;
	const int VALUE_MAX = 255;
	int value, saturation, hue;
	for (int i = 0; i < src.rows; i++){
		for (int j = 0; j < src.cols; j++){
			value = src.at<Vec3b>(i, j)[2];
			saturation = src.at<Vec3b>(i, j)[1];
			hue = src.at<Vec3b>(i, j)[0];
			if ((value >= VALUE_MIN) && (value <= VALUE_MAX) && (saturation >= SATURATION_MIN) && (saturation <= SATURATION_MAX) && ((hue >= HUE_MIN) || (hue <= HUE_MAX))){
				borders.at<uchar>(i, j) = 255;
			}
			else{
				borders.at<uchar>(i, j) = 0;
			}
		}
	}

	// fill the small holes
	dilate(borders, borders, Mat(), Point(-1, -1), 2, 1, 1);

	vector<vector<Point>> contours;
	vector<Vec4i> hierarchy;
	int numLabels;

	// Documentation for finding contours: http://docs.opencv.org/modules/imgproc/doc/structural_analysis_and_shape_descriptors.html?highlight=findcontours#findcontours
	findContours(borders, contours, hierarchy, RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));

	// Documentation for drawing contours: http://docs.opencv.org/modules/imgproc/doc/structural_analysis_and_shape_descriptors.html?highlight=drawcontours#drawcontours
	drawContours(dst1, contours, -1, Scalar(255, 255, 255), CV_FILLED, 8, hierarchy);

	// clean up the tiny objects
	erode(dst1, dst1, Mat(), Point(-1, -1), 2, 1, 1);

	// Connected component labelling: http://docs.opencv.org/3.0-beta/modules/imgproc/doc/structural_analysis_and_shape_descriptors.html
	Mat centroidOutput, a, b;	// a and b are just place holders (not needed later)
	numLabels = connectedComponentsWithStats(dst1, a, b, centroidOutput, 8, CV_16U);
	cvtColor(dst1, dst2, CV_GRAY2BGR);
	vector<float> centroid;
	for (int i = 1; i < numLabels; i++)
	{
		//Point p(centroidOutput.at<double>(i, 0), centroidOutput.at<double>(i, 1));
		centroid.clear();
		centroid.push_back((float)centroidOutput.at<double>(i, 0));
		centroid.push_back((float)centroidOutput.at<double>(i, 1));
		data.push_back(centroid);
		//draw red circles on the image
		//circle(dst2, p, 3, Scalar(0, 0, 255), -1, 8);
	}
	return;
}


// functions provided in lab 7
string getImagePath(string dir, int imageNum, string extension)
{
	ostringstream oss;
	oss << imageNum;
	dir += "/" + oss.str();
	dir += "." + extension;
	return dir;
}

void convertFileToMat(String filename, Mat& labelled, Mat& binary)
{
	//read file
	ifstream infile(filename);
	vector <vector <int> > data;
	if (!infile){
		cout << "Error reading file!";
		return;
	}

	//read the comma separated values into a vector of vector of ints
	while (infile)
	{
		string s;
		if (!getline(infile, s)) break;

		istringstream ss(s);
		vector <int> datarow;

		while (ss)
		{
			string srow;
			int sint;
			if (!getline(ss, srow, ',')) break;
			sint = atoi(srow.c_str()); //convert string to int
			datarow.push_back(sint);
		}
		data.push_back(datarow);
	}

	//construct the labelled matrix from the vector of vector of ints
	labelled = Mat::zeros(data.size(), data.at(0).size(), CV_8UC1);
	for (int i = 0; i < labelled.rows; ++i)
		for (int j = 0; j < labelled.cols; ++j)
			labelled.at<uchar>(i, j) = data.at(i).at(j);

	//construct the binary matrix from the labelled matrix
	binary = Mat::zeros(labelled.rows, labelled.cols, CV_8UC1);
	for (int i = 0; i < labelled.rows; ++i)
		for (int j = 0; j < labelled.cols; ++j)
			binary.at<uchar>(i, j) = (labelled.at<uchar>(i, j) == 0) ? 0 : 255;

}

void drawObjectDetections(String filename, Mat& binary3channel, vector <vector <float> > &data)
{
	ifstream infile(filename);

	if (!infile)
	{
		cout << "Error reading file!";
		return;
	}

	//read the comma separated values into a vector of vector of ints
	while (infile)
	{
		string s;
		if (!getline(infile, s)) break;

		istringstream ss(s);
		vector <float> datarow;

		while (ss)
		{
			string srow;
			int sint;
			if (!getline(ss, srow, ',')) break;
			sint = atoi(srow.c_str()); //convert string to int
			datarow.push_back(sint);
		}
		data.push_back(datarow);
	}
	//draw red circles on the image
	/*for (int i = 0; i < data.size(); ++i)
	{
		Point center(data.at(i).at(0), data.at(i).at(1));
		circle(binary3channel, center, 3, Scalar(0, 0, 255), -1, 8);
	}*/
}