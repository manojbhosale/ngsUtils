package liftoverutils;

//Interval tree implementation
import java.util.ArrayList;
import java.util.List;

class IntervalTree<Type> {

	private IntervalNode<Type> head;
	private List<Interval<Type>> intervalList;
	private boolean inSync;
	private int size;

	public IntervalTree() {
		this.head = new IntervalNode<Type>();
		this.intervalList = new ArrayList<Interval<Type>>();
		this.inSync = true;
		this.size = 0;
	}

	public IntervalTree(List<Interval<Type>> intervalList) {
		this.head = new IntervalNode<Type>(intervalList);
		this.intervalList = new ArrayList<Interval<Type>>();
		this.intervalList.addAll(intervalList);
		this.inSync = true;
		this.size = intervalList.size();
	}

	public List<Type> get(long time) {
		List<Interval<Type>> intervals = getIntervals(time);
		List<Type> result = new ArrayList<Type>();
		for (Interval<Type> interval : intervals)
			result.add(interval.getData());
		return result;
	}

	public List<Interval<Type>> getIntervals(long time) {
		build();
		return head.stab(time);
	}

	public List<Interval<Type>> get(long start, long end) {
		List<Interval<Type>> intervals = getIntervals(start, end);
		/*
		 * List<Type> result = new ArrayList<Type>(); for (Interval<Type>
		 * interval : intervals) result.add(interval.getData());
		 */return intervals;
	}

	public List<Interval<Type>> getIntervals(long start, long end) {
		build();
		return head.query(new Interval<Type>(start, end, null));
	}

	public void addInterval(Interval<Type> interval) {
		intervalList.add(interval);
		inSync = false;
	}

	public void addInterval(long begin, long end, Type data) {
		intervalList.add(new Interval<Type>(begin, end, data));
		inSync = false;
	}

	public boolean inSync() {
		return inSync;
	}

	public void build() {
		if (!inSync) {
			head = new IntervalNode<Type>(intervalList);
			inSync = true;
			size = intervalList.size();
		}
	}

	public int currentSize() {
		return size;
	}

	public int listSize() {
		return intervalList.size();
	}

	@Override
	public String toString() {
		return nodeString(head, 0);
	}

	private String nodeString(IntervalNode<Type> node, int level) {
		if (node == null)
			return "";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < level; i++)
			sb.append("\t");
		sb.append(node + "\n");
		sb.append(nodeString(node.getLeft(), level + 1));
		sb.append(nodeString(node.getRight(), level + 1));
		return sb.toString();
	}
}
