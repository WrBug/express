package cn.mandroid.express.Utils1;

import java.util.Comparator;

import cn.mandroid.express.Model1.Bean1.Content;

public class PinyinComparator implements Comparator<Content> {

	public int compare(Content o1, Content o2) {
		if (o1.getLetter().equals("@")
				|| o2.getLetter().equals("#")) {
			return -1;
		} else if (o1.getLetter().equals("#")
				|| o2.getLetter().equals("@")) {
			return 1;
		} else {
			return o1.getLetter().compareTo(o2.getLetter());
		}
	}

}
