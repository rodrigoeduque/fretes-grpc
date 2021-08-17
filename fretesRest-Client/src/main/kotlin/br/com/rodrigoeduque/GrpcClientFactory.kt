package br.com.rodrigoeduque

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun fretesClientStub(@GrpcChannel(value = "fretes") channel: ManagedChannel): FretesServiceGrpc.FretesServiceBlockingStub? {

        return FretesServiceGrpc.newBlockingStub(channel)

    }
}