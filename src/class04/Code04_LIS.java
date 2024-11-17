package class04;

public class Code04_LIS {
	/**
	 * 最长递增子序列（可以是不连续，但先后顺序要一样）
	 * 方法：
	 * 1。获取每个节点，后面有多少个小于arr[i]的数
	 *
	 */

	public static int[] lis1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return null;
		}
		//必须以i结尾，最长递归子序列的长度
		int[] dp = getdp1(arr);
		return generateLIS(arr, dp);
	}

	public static int[] getdp1(int[] arr) {
		int[] dp = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			dp[i] = 1;
			for (int j = 0; j < i; j++) {
				if (arr[i] > arr[j]) {
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
		}
		return dp;
	}

	public static int[] generateLIS(int[] arr, int[] dp) {
		int len = 0;
		int index = 0;
		//获取最大的长度
		for (int i = 0; i < dp.length; i++) {
			if (dp[i] > len) {
				len = dp[i];
				index = i;
			}
		}
		//递增子序列数组
		int[] lis = new int[len];
		//从后向前遍历，依次找到各个元素
		lis[--len] = arr[index];
		for (int i = index; i >= 0; i--) {
			if (arr[i] < arr[index] && dp[i] == dp[index] - 1) {
				lis[--len] = arr[i];
				index = i;
			}
		}
		return lis;
	}

	public static int[] lis2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return null;
		}
		int[] dp = getdp2(arr);
		return generateLIS(arr, dp);
	}

	public static int[] getdp2(int[] arr) {
		//必须以i结尾最长子序列的长度
		int[] dp = new int[arr.length];
		//找到的所有长度为i+1的递增子序列中最小结尾是什么值
		int[] ends = new int[arr.length];
		ends[0] = arr[0];
		dp[0] = 1;
		int right = 0; // 0....right   right往右无效
		int l = 0;
		int r = 0;
		int m = 0;
		for (int i = 1; i < arr.length; i++) {
			l = 0;
			r = right;
			//ends是有序的数组
			//找到大于等于arr[i],最左的位置
			while (l <= r) {
				m = (l + r) / 2;
				if (arr[i] > ends[m]) {
					l = m + 1;
				} else {
					r = m - 1;
				}
			}
			// 如果ends中没有大于等于arr[i]的数据，l -> right+1
			right = Math.max(right, l);
			ends[l] = arr[i];
			//dp保存的是i位置最长子序列的长度，l是下标，计算长度的时候要+1
			dp[i] = l + 1;
		}
		return dp;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i != arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] arr = { 2, 1, 5, 3, 6, 4, 8, 9, 7 };
		printArray(arr);
		printArray(lis1(arr));
		printArray(lis2(arr));

	}
}