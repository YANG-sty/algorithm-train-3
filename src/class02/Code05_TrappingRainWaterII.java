package class02;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Code05_TrappingRainWaterII {

	/**
	 * 提供的是二维数组，形象地理解，就是提供了一个立体三维得柱形容器，求该容器所能容纳的最大体积。
	 * 由于水是往低处流的， 所以对于这一类trapping water问题，我们只用从最外层开始往内接雨水就可以。
	 *
	 * 首先从矩阵的最外层中找到最小的柱子，可以通过堆来实现，当堆不为空的情况下，每次弹出的都是高度最小的柱子，
	 * 这时候从该柱子出发，遍历其周边的四个方向（BSF）的柱子，如果某个柱子未到达或超出边界且尚未被访问，
	 * 则将该柱子加入堆中，如果该柱子的高度比当前柱子高度小，则更新该柱子的高度，同时记录此处所容纳的水，直至堆为空。
	 *
	 * 方法：小根堆
	 * 时间复杂度O(N*M*log(N*M))
	 *
	 */

	public static class Node {
		public int value;
		public int row;
		public int col;

		public Node(int v, int r, int c) {
			value = v;
			row = r;
			col = c;
		}

	}

	public static class NodeComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			return o1.value - o2.value;
		}

	}

	public static int trapRainWater(int[][] heightMap) {
		if (heightMap == null || heightMap.length == 0 || heightMap[0] == null || heightMap[0].length == 0) {
			return 0;
		}
		int N = heightMap.length;
		int M = heightMap[0].length;
		// isEnter[i][j] == true  之前进过
		//  isEnter[i][j] == false 之前没进过
		//是否遍历过
		boolean[][] isEnter = new boolean[N][M];
		// 小根堆
		PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
		//将左外层的一圈添加到堆中
		for (int col = 0; col < M - 1; col++) {
			isEnter[0][col] = true;
			heap.add(new Node(heightMap[0][col], 0, col));
		}
		for (int row = 0; row < N - 1; row++) {
			isEnter[row][M - 1] = true;
			heap.add(new Node(heightMap[row][M - 1], row, M - 1));
		}
		for (int col = M - 1; col > 0; col--) {
			isEnter[N - 1][col] = true;
			heap.add(new Node(heightMap[N - 1][col], N - 1, col));
		}
		for (int row = N - 1; row > 0; row--) {
			isEnter[row][0] = true;
			heap.add(new Node(heightMap[row][0], row, 0));
		}


		/**
		 * 将节点放到堆中的时候计算当前节点的蓄水量
		 */
		int water = 0; // 每个位置的水量，累加到water上去
		int max = 0; // 每个node在弹出的时候，如果value更大，更新max，否则max的值维持不变
		//因为是小根堆，每次弹出的节点都是当前最小的值，最小值决定水的下限
		while (!heap.isEmpty()) {
			//弹出的当前的最小的点
			Node cur = heap.poll();
			//获取较大的max，如果最小点大于max，表示出现了一个新的凹槽，以这个点为下限，计算周围蓄水数量
			max = Math.max(max, cur.value);
			int r = cur.row;
			int c = cur.col;
			if (r > 0 && !isEnter[r - 1][c]) { // 如果有上面的位置并且上面位置没进过堆
				water += Math.max(0, max - heightMap[r - 1][c]);
				isEnter[r - 1][c] = true;
				heap.add(new Node(heightMap[r - 1][c], r - 1, c));
			}
			if (r < N - 1 && !isEnter[r + 1][c]) {
				water += Math.max(0, max - heightMap[r + 1][c]);
				isEnter[r + 1][c] = true;
				heap.add(new Node(heightMap[r + 1][c], r + 1, c));
			}
			if (c > 0 && !isEnter[r][c - 1]) {
				water += Math.max(0, max - heightMap[r][c - 1]);
				isEnter[r][c - 1] = true;
				heap.add(new Node(heightMap[r][c - 1], r, c - 1));
			}
			if (c < M - 1 && !isEnter[r][c + 1]) {
				water += Math.max(0, max - heightMap[r][c + 1]);
				isEnter[r][c + 1] = true;
				heap.add(new Node(heightMap[r][c + 1], r, c + 1));
			}
		}
		return water;
	}

}
