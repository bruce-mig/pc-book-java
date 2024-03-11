package com.gitlab.brucemig.pcbook.service;

import com.gitlab.brucemig.pcbook.pb.CreateLaptopRequest;
import com.gitlab.brucemig.pcbook.pb.CreateLaptopResponse;
import com.gitlab.brucemig.pcbook.pb.Laptop;
import com.gitlab.brucemig.pcbook.pb.LaptopServiceGrpc;
import com.gitlab.brucemig.pcbook.sample.Generator;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.Rule;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class LaptopServerTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule(); // automatic graceful shutdown channel at the end of test

    private LaptopStore store;
    private LaptopServer server;
    private ManagedChannel channel;

    @BeforeEach
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        InProcessServerBuilder serverBuilder = InProcessServerBuilder.forName(serverName).directExecutor();

        store = new InMemoryLaptopStore();
        server = new LaptopServer(serverBuilder, 0, store);
        server.start();

        channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build()
        );
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void createLaptopWithAValidID() {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptop();
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(channel);
        CreateLaptopResponse response = stub.createLaptop(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(laptop.getId(), response.getId());

        Laptop found = store.Find(response.getId());
        Assertions.assertNotNull(found);
    }

    @Test
    public void createLaptopWithAnEmptyD() {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptop().toBuilder().setId("").build();
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(channel);
        CreateLaptopResponse response = stub.createLaptop(request);
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getId().isEmpty());

        Laptop found = store.Find(response.getId());
        Assertions.assertNotNull(found);
    }

    @Test
    public void createLaptopWithAnInvalidID() {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptop().toBuilder().setId("invalid").build();
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(channel);

        assertThrows(StatusRuntimeException.class, () -> {
            stub.createLaptop(request);
        });
    }

    @Test
    public void createLaptopWithAnIAlreadyExistingID() throws Exception {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptop();
        store.Save(laptop);
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        LaptopServiceGrpc.LaptopServiceBlockingStub stub = LaptopServiceGrpc.newBlockingStub(channel);
        assertThrows(StatusRuntimeException.class, () -> {
            stub.createLaptop(request);
        });
    }
}