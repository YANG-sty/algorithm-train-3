package class01;

public class Code05_MaxOneBorderSize {
	/**
	 * 边框都是1的最大正方形
	 * N*N
	 * 长方形个数 O(N^4)
	 * 正方形个数 O(N^3)
	 * 引入2个数组，right[i][j]表示从(i,j)往右，有多少个连续1，down[i][j]表示从(i,j)往下，有多少个连续1
	 */

	public static void setBorderMap(int[][] m, int[][] right, int[][] down) {
		int r = m.length;
		int c = m[0].length;
		if (m[r - 1][c - 1] == 1) {
			right[r - 1][c - 1] = 1;
			down[r - 1][c - 1] = 1;
		}
		for (int i = r - 2; i != -1; i--) {
			if (m[i][c - 1] == 1) {
				right[i][c - 1] = 1;
				down[i][c - 1] = down[i + 1][c - 1] + 1;
			}
		}
		for (int i = c - 2; i != -1; i--) {
			if (m[r - 1][i] == 1) {
				right[r - 1][i] = right[r - 1][i + 1] + 1;
				down[r - 1][i] = 1;
			}
		}
		for (int i = r - 2; i != -1; i--) {
			for (int j = c - 2; j != -1; j--) {
				if (m[i][j] == 1) {
					right[i][j] = right[i][j + 1] + 1;
					down[i][j] = down[i + 1][j] + 1;
				}
			}
		}
	}

	public static int getMaxSize(int[][] m) {
		int[][] right = new int[m.length][m[0].length];
		int[][] down = new int[m.length][m[0].length];
		setBorderMap(m, right, down); // O(N^2); + 
		
		for (int size = Math.min(m.length, m[0].length); size != 0; size--) {
			if (hasSizeOfBorder(size, right, down)) {
				return size;
			}
		}
		return 0;
	}

	public static boolean hasSizeOfBorder(int size, int[][] right, int[][] down) {
		//要组成矩形，点不能都在一个位置
		for (int i = 0; i != right.length - size + 1; i++) { // 行
			for (int j = 0; j != right[0].length - size + 1; j++) { // 列
				if (right[i][j] >= size //当前点，右边有连续1的个数
						&& down[i][j] >= size //当前点，下边连续1的根数
						&& right[i + size - 1][j] >= size //左下点，右边连续1的个数
						&& down[i][j + size - 1] >= size) { //右上点，下边连续1的个数
					return true;
				}
			}
		}
		return false;
	}
	
	public static int[][] generateRandom01Matrix(int rowSize, int colSize) {
		int[][] res = new int[rowSize][colSize];
		for (int i = 0; i != rowSize; i++) {
			for (int j = 0; j != colSize; j++) {
				res[i][j] = (int) (Math.random() * 2);
			}
		}
		return res;
	}

	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i != matrix.length; i++) {
			for (int j = 0; j != matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int[][] matrix = generateRandom01Matrix(7, 8);
		printMatrix(matrix);
		System.out.println(getMaxSize(matrix));
	}
}
