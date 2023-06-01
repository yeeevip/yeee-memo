package vip.yeee.memo.integrate.jdk.base.problem;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/6/1 9:17
 */
@Slf4j
public class HashMapResizeExample {

    public static void main(String[] args) throws InterruptedException {
        CusHashMap<String, String> cusHashMap = new CusHashMap<>(1);

        cusHashMap.putVal(0, "1", "1");
        cusHashMap.putVal(0, "2", "2");
        cusHashMap.putVal(0, "3", "3");
        cusHashMap.showBucketEle(0);

//        oneThreadResize_1_7(cusHashMap);

//        twoThreadResize_1_7(cusHashMap);

//        oneThreadResize_1_8(cusHashMap);

        twoThreadResize_1_8(cusHashMap);
    }

    public static void oneThreadResize_1_7(CusHashMap<String, String> map) {
        map.transfer_1_7();
        map.showBucketEle(0);
    }

    public static void twoThreadResize_1_7(CusHashMap<String, String> map) throws InterruptedException {
        /*
            oldTable：1 -> 2 -> 3
            核心代码：【头插法】
                      e = table[i]
                      do {
                            Node<K,V> next = e.next;
                            e.next = newTable[i];
                            newTable[target_i] = e;
                            e = next;
                        } while (e != null)
            ------------------------------------------------------------------------------------------------------------------------------------------------------
                          |            t1            |                 t2                  |               t3
            ------------------------------------------------------------------------------------------------------------------------------------------------------
            T1 newTable： |    e = 1 , next = 2;     |                                     |   loop 1： -> 1 -> null (e = next = 2, next = 2.next = 1)
                          |                          |                                     |   loop 2:  -> 2 -> 1 -> null (e = next = 1, next = 1.next = null)
                          |                          |                                     |   loop 3:  -> 1 -> 2 -> 1  return
            ------------------------------------------------------------------------------------------------------------------------------------------------------
            T2 newTable： |                          | 执行完扩容：-> 3 -> 2 -> 1 -> null   |
            ------------------------------------------------------------------------------------------------------------------------------------------------------
         */
        new Thread(() -> map.transfer_1_7(5)).start(); // 执行到e.next = (Node<K, V>) newTable[i]; 暂停。等Thread2执行完再执行
        new Thread(map::transfer_1_7).start();
        TimeUnit.SECONDS.sleep(10);
        map.showBucketEle(0);
    }

    public static void oneThreadResize_1_8(CusHashMap<String, String> map) {
        map.transfer_1_8();
        map.showBucketEle(0);
    }

    public static void twoThreadResize_1_8(CusHashMap<String, String> map) throws InterruptedException {
        /*
            oldTable：1 -> 2 -> 3
            核心代码：【尾插法】
                      e = table[i]
                      Node<K,V> loHead = null, loTail = null; // 记录新链的头尾
                      do {
                            Node<K,V> next = e.next;
                            if (loTail == null) {
                                loHead = e;
                            } else {
                                loTail.next = e;
                            }
                            loTail = e;
                            e = next;
                        } while (e != null)
                      // 将新链放置新数组槽下
                      loTail.next = null;
                      newTable[target_i] = loHead;
         */
        new Thread(() -> map.transfer_1_8(5)).start(); // 执行到e.next = (Node<K, V>) newTable[i]; 暂停。等Thread2执行完再执行
        new Thread(map::transfer_1_8).start();
        TimeUnit.SECONDS.sleep(10);
        map.showBucketEle(0);
    }

    static class CusHashMap<K,V> {

        private Node<K,V>[] table;

        public CusHashMap(int bucketCapacity) {
            this.table = new Node[bucketCapacity];
        }

        static class Node<K,V> implements Map.Entry<K,V> {
            int hash;
            final K key;
            V value;
            Node<K,V> next;

            Node(int hash, K key, V value) {
                this.hash = hash;
                this.key = key;
                this.value = value;
            }

            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return value;
            }

            @Override
            public V setValue(V value) {
                return null;
            }

            public void setNext(Node<K, V> next) {
                this.next = next;
            }

            public Node<K, V> getNext() {
                return next;
            }

            @Override
            public String toString() {
                return "Node{" +
                        "key=" + key +
                        ", value=" + value +
//                        ", next=" + next +
                        '}';
            }
        }

        public void putVal(int index, K k, V v) {
            Node<K, V> e;
            int hash = index;
            if ((e = (Node<K, V>)table[index]) == null) {
                table[index] = new Node<>(hash, k, v);
            } else {
                while (e.next != null) {
                    e = e.next;
                }
                e.next = new Node<>(hash, k, v);
            }
        }

        public void showBucketEle(int index) {
            Node<K, V> e = table[index];
            while (e != null) {
                log.info("元素 - table = {}, K = {}，V = {}", table, e.getKey(), e.getValue());
                e = e.next;
            }
        }

        public void transfer_1_7() {
            transfer_1_7(0);
        }

        public void transfer_1_7(long sleep) {
            Node<K,V>[] newTable = new Node[table.length];
            for (int i = 0; i < table.length; i++) {
                Node<K,V> e = (Node<K, V>)table[i];
                if (e != null) {
                    try {
                        // 这里是让两个线程同时执行完前面的代码
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    table[i] = null;
                    do {
                        Node<K,V> next = e.next;
                        int target_i = i;
                        try {
                            TimeUnit.SECONDS.sleep(sleep);
                            sleep = 0;
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        log.info("转移 - e = {}，e.next = {}", e, next);
                        e.next = (Node<K, V>) newTable[i];
                        newTable[target_i] = e;
                        e = next;
                    } while (e != null);
                }
            }
            table = newTable;
        }

        public void transfer_1_8() {
            transfer_1_8(0);
        }

        public void transfer_1_8(long sleep) {
            Node<K,V>[] newTable = new Node[table.length];
            for (int i = 0; i < table.length; i++) {
                Node<K,V> e = (Node<K, V>)table[i];
                if (e != null) {
                    try {
                        // 这里是让两个线程同时执行完前面的代码
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    table[i] = null;
                    Node<K,V> loHead = null, loTail = null;
                    int target_i = i;
                    do {
                        Node<K,V> next = e.next;
                        try {
                            TimeUnit.SECONDS.sleep(sleep);
                            sleep = 0;
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        log.info("转移 - e = {}，e.next = {}", e, next);
                        if (loTail == null) {
                            loHead = e;
                        } else {
                            loTail.next = e;
                        }
                        loTail = e;
                        e = next;
                    } while (e != null);
                    loTail.next = null;
                    newTable[target_i] = loHead;
                }
            }
            table = newTable;
        }

    }

}
