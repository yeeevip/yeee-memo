package vip.yeee.memo.demo.reactive.prog.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author https://www.yeee.vip
 * @since 2021/11/22 15:24
 */
public class ReactorSomeApisTest {


    @Test
    public void testCreateFluxFromExistingData() {
        Flux<Integer> justFlux = Flux.just(1, 2, 3, 4, 5, 6);
        subscriptFlux("justFlux", justFlux);
    }

    @Test
    public void testCreateFluxProgrammatically() {
        Flux<Object> generateFlux = Flux.generate(() -> 1, (state, sink) -> {
            sink.next("message # " + state);
            if (state == 10) {
                sink.complete();
            }
            return state + 1;
        });
        subscriptFlux("generateFlux", generateFlux);
    }

    @Test
    public void createMonoAsync() {
        Mono<String> callableMono = Mono.fromCallable(() -> Thread.currentThread().getName() + "@ " + LocalDateTime.now())
                .publishOn(Schedulers.elastic());
        blockMono("callableMono", callableMono);

        Mono<Object> runnableMono = Mono.fromRunnable(() -> System.out.println(Thread.currentThread().getName() + "@ " + LocalDateTime.now()))
                .publishOn(Schedulers.elastic());
        blockMono("runnableMono", runnableMono);

        Mono<String> supplierMono = Mono.fromSupplier(() -> Thread.currentThread().getName() + "@ " + LocalDateTime.now())
                .publishOn(Schedulers.elastic());
        blockMono("supplierMono", supplierMono);
    }

    @Test
    public void createMonoFromExistingData() {
        Mono<Integer> justMono = Mono.just(1);
        blockMono("justMono", justMono);
    }

    @Test
    public void mapVsFlatMap() {
        Flux<String> mapFlux = Flux.just(1, 2, 3).map(i -> "id #" + i);
        subscriptFlux("mapFlux", mapFlux);
        Flux<String> flatMapFlux = Flux.just(1, 2, 3).flatMap(i -> Mono.just("id #" + i));
        subscriptFlux("flatMapFlux", flatMapFlux);
    }

    @Test
    public void useThenForFlow() {
        Mono<String> thenMono = Mono.just("world")
                .map(n -> "hello " + n)
                .doOnNext(System.out::println)
                .thenReturn("do something else");
        blockMono("thenMono", thenMono);
    }

    @Test
    public void monoFluxInterchange() {
        Flux<Integer> monoFlux = Mono.just(1).flux();
        subscriptFlux("monoFlux", monoFlux);
        Mono<List<Integer>> fluxMono = Flux.just(1, 2, 3).collectList();
        blockMono("fluxMono", fluxMono);
    }

    @Test
    public void zipMonoOrFlux() {
        String userId = "max";
        Mono<String> monoProfile = Mono.just(userId + "的详细信息");
        Mono<String> monoLatestOrder = Mono.just(userId + "的最新订单");
        Mono<String> monoLatestReview = Mono.just(userId + "的最新评论");
        Mono<Tuple3<String, String, String>> zipMono = Mono.zip(monoProfile, monoLatestOrder, monoLatestReview)
                .doOnNext(t -> System.out.printf("%s的主页，%s，%s，%s%n", userId, t.getT2(), t.getT2(), t.getT3()));
        blockMono("zipMono", zipMono);
    }

    @Test
    public void errorHandling() {
        Flux<String> throwExceptionFlux = Flux.range(1, 10).map(i -> {
            if (i > 5) {
                throw new RuntimeException("Something wrong");
            }
            return "item #" + i;
        });
        subscriptFlux("throwExceptionFlux", throwExceptionFlux);

        Flux<String> errorFlux = Flux.range(1, 10).flatMap(i -> {
            if (i > 5) {
                return Mono.error(new RuntimeException("Something wrong"));
            }
            return Mono.just("item #" + i);
        });
        subscriptFlux("errorFlux", errorFlux);
    }

    private void blockMono(String varName, Mono<?> mono) {
        mono.doOnSubscribe(s -> System.out.println(varName + "：OnSubscribe"))
                .doOnNext(e -> System.out.println("received：" + e))
                .block();
    }

    private void subscriptFlux(String varName, Flux<?> flux) {
        flux.doOnSubscribe(s -> System.out.println(varName + "：OnSubscribe"))
                .doOnNext(e -> System.out.println("received：" + e))
                .doOnComplete(() -> System.out.println(varName + "：completed！"))
                .subscribe();
    }

}
