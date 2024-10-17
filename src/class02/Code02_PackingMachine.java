package class02;

public class Code02_PackingMachine {

	/**
	 * 每次移动所有的机器都可以同时进行操作，进行获取的移动次数最少
	 * 以i为当前位置
	 * 左侧的需要移动的数量为a，右侧移动的数量为b
	 * a，b都大于0的时候，取最大值
	 * 当a，b都小于0的时候，说明需要从i位置分别向左右两遍进行移动，这个时候总的移动次数为 a，b绝对值的和
	 */
	public static int MinOps(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int size = arr.length;
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += arr[i];
		}
		if (sum % size != 0) {
			return -1;
		}
		int avg = sum / size;
		int leftSum = 0;
		int ans = 0;
		// 每个位置都求各自的
		for (int i = 0; i < arr.length; i++) {
			// i号机器，是中间机器，左(0~i-1) i 右(i+1~N-1)
			// 负 需要输入     正需要输出 
			int leftRest = leftSum - i * avg; // a-b
			// 负 需要输入     正需要输出 
			// c - d
			int rightRest =  (sum - leftSum - arr[i]) -  (size - i - 1) * avg; 
			if (leftRest < 0 && rightRest < 0) {
				ans = Math.max(ans, Math.abs(leftRest) + Math.abs(rightRest));
			} else {
				ans = Math.max(ans, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
			}
			leftSum += arr[i];
		}
		return ans;
	}

}
