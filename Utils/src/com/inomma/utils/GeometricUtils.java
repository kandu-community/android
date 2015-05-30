package com.inomma.utils;

public class GeometricUtils {

	public enum PositionOnCircle {
		INSIDE, ONEDGE, OUTSIDE
	}
	
	public static double distance(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public static class Circular {
		
		public static int getSector(double x, double y, int count, boolean wrap)
		{
			double distanceFromZero = Math.sqrt(x*x + y*y);
			double radian = Math.acos(x/distanceFromZero);
			if(y < 0)
				radian = 2*Math.PI - radian;
			return (int)(radian/(2*Math.PI / count) + (wrap ? Math.PI/count : 0));
		}

		public static double itemRadiusForCount(double circleRadius, int count,
				PositionOnCircle position) {
			if (circleRadius <= 0 || count <= 2)
				return -1;
			switch (position) {
			case OUTSIDE:

				return circleRadius / (1 - Math.sin(Math.PI / count)) - circleRadius;
			case ONEDGE:
				return circleRadius * Math.tan(Math.PI / count);
			default:
				return -1;
			}

		}

		public static int fitCount(double circleRadius, double itemRadius) {
			return fitCount(circleRadius, itemRadius, PositionOnCircle.ONEDGE);
		}

		public static int fitCount(double circleRadius, double itemRadius,
				PositionOnCircle position) {

			if (circleRadius <= 0 || itemRadius <= 0)
				return -1;
			int k = position.ordinal() - 1;
			return (int) (Math.PI / Math.asin(itemRadius / (circleRadius + k * itemRadius)));
		}
	}
}
