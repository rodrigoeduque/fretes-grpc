package br.com.rodrigoeduque

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject

@Controller
class GRpcServerController(@Inject val grpcServer: GrpcEmbeddedServer) {

    @Get("/grpc-server/stop")
    fun stop() : HttpResponse<String> {
        grpcServer.stop()

        return HttpResponse.ok("Is Running? ${grpcServer.isRunning}")

    }
}