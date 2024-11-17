package class02;

public class Code04_TrappingRainWater {

	/**
	 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
	 * 给定一个正整数数组arr，把arr想象成一个直方图。返回这个直方图如果装水，能装下几格水？
	 *
	 * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
	 * 输出：6
	 * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
	 *
	 * 输入：height = [4,2,0,3,2,5]
	 * 输出：9
	 *
	 * 类似单调栈
	 * 使用双指针分别从左/右两边出发，遇到更大的值进行更新
	 * 左 <= 右 =》先结算左边，比较最大值
	 * 左 > 右 =》先结算右边，比较最大值
	 *
	 */

	public static int water1(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		int water = 0;
		//
		for (int i = 1; i < N - 1; i++) {
			int leftMax = Integer.MIN_VALUE;
			for (int j = 0; j < i; j++) {
				leftMax = Math.max(leftMax, arr[j]);
			}
			int rightMax = Integer.MIN_VALUE;
			for (int j = i + 1; j < N; j++) {
				rightMax = Math.max(rightMax, arr[j]);
			}
			water += Math.max(Math.min(leftMax, rightMax) - arr[i], 0);
		}
		return water;
	}

	public static int water2(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		//每个节点位置，左侧最大的值
		int[] leftMaxs = new int[N];
		leftMaxs[0] = arr[0];
		for (int i = 1; i < N; i++) {
			leftMaxs[i] = Math.max(leftMaxs[i - 1], arr[i]);
		}

		//每个节点为值，右侧最大的值
		int[] rightMaxs = new int[N];
		rightMaxs[N - 1] = arr[N - 1];
		for (int i = N - 2; i >= 0; i--) {
			rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
		}
		int water = 0;
		for (int i = 1; i < N - 1; i++) {
			water += Math.max(Math.min(leftMaxs[i - 1], rightMaxs[i + 1]) - arr[i], 0);
		}
		return water;
	}

	public static int water3(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		//每个节点，右侧最大的值
		int[] rightMaxs = new int[N];
		rightMaxs[N - 1] = arr[N - 1];
		for (int i = N - 2; i >= 0; i--) {
			rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
		}
		int water = 0;
		int leftMax = arr[0];
		for (int i = 1; i < N - 1; i++) {
			//结算当前节点的值
			water += Math.max(Math.min(leftMax, rightMaxs[i + 1]) - arr[i], 0);
			//每个节点，左侧最大的值
			leftMax = Math.max(leftMax, arr[i]);
		}
		return water;
	}

	public static int water4(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		int L = 1;
		//左侧最大值
		int leftMax = arr[0];
		int R = N - 2;
		//右侧最大值
		int rightMax = arr[N - 1];
		int water = 0;
		while (L <= R) {
			/**
			 * 哪一侧的值小，对哪一侧进行数据结算，获取下一个最大值
			 * 节点，左右两侧的最小值，决定了该节点能存多少水
			 */
			if (leftMax <= rightMax) {
				water += Math.max(0, leftMax - arr[L]);
				leftMax = Math.max(leftMax, arr[L++]);
			} else {
				water += Math.max(0, rightMax - arr[R]);
				rightMax = Math.max(rightMax, arr[R--]);
			}
		}
		return water;
	}

	// for test
	public static int[] generateRandomArray(int len, int value) {
		int[] ans = new int[(int) (Math.random() * len) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * value) + 1;
		}
		return ans;
	}

	public static void main(String[] args) {
		int len = 100;
		int value = 200;
		int testTimes = 100000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			int[] arr = generateRandomArray(len, value);
			int ans1 = water1(arr);
			int ans2 = water2(arr);
			int ans3 = water3(arr);
			int ans4 = water4(arr);
			if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
