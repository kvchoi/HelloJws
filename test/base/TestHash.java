package base;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import utils.JumpConsistentHash;

public class TestHash {

    private final static String file = "D:\\workspace6\\HelloJws\\test\\uuid.log";
    private final static Map<Integer, AtomicInteger> count = Maps.newConcurrentMap();
    private final static int buckets = 100;

    public static void main(String[] args) throws Exception {
        LineIterator li = FileUtils.lineIterator(new File(file), CharEncoding.UTF_8);
        while (li.hasNext()) {
            String src = li.nextLine();
            testhash3(src);
        }
        System.out.println(count);
    }

    public static void testhash1(String src) {
        src = StringUtils.trim(src);
        HashFunction hf = Hashing.adler32();
        HashCode hc = hf.newHasher().putString(src).hash();
        int b = Hashing.consistentHash(hc, buckets);
        if (count.containsKey(Integer.valueOf(b))) {
            AtomicInteger ai = count.get(Integer.valueOf(b));
            ai.addAndGet(1);
        } else {
            count.put(Integer.valueOf(b), new AtomicInteger(1));
        }
    }

    public static void testhash2(String src) {
        src = StringUtils.trim(src);
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher().putString(src).hash();
        int b = Hashing.consistentHash(hc, buckets);
        if (count.containsKey(Integer.valueOf(b))) {
            AtomicInteger ai = count.get(Integer.valueOf(b));
            ai.addAndGet(1);
        } else {
            count.put(Integer.valueOf(b), new AtomicInteger(1));
        }
    }

    public static void testhash3(String src) {
        int b = JumpConsistentHash.jumpConsistentHash(src, buckets);
        if (count.containsKey(Integer.valueOf(b))) {
            AtomicInteger ai = count.get(Integer.valueOf(b));
            ai.addAndGet(1);
        } else {
            count.put(Integer.valueOf(b), new AtomicInteger(1));
        }

    }
}
