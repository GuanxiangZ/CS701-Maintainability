package kalah;

import java.util.ArrayList;

public class OverrideArraryList<E> extends ArrayList<E> {
	
	@Override
	public E get(int index) {
		int realIndex = index % size();
		return super.get(realIndex);
    }
}
