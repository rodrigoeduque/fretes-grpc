package br.com.rodrigoeduque

import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FretesGrpcServer : FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FretesGrpcServer::class.java)

    override fun calculaFrete(
        request: CalcularFretesRequest?,
        responseObserver: StreamObserver<CalcularFretesResponse>?
    ) {
        logger.info("Calculando Frete para request : $request")

        val cep = request?.cep
        if (cep == null || cep.isBlank()) {
            val e =
                Status.INVALID_ARGUMENT
                    .withDescription("CEP deve ser informado")
                    .asRuntimeException()
            responseObserver?.onError(e)
        }
        if (!cep!!.matches("[0-9]{5}-[0-9]{3}".toRegex())) {
            val e = Status.INVALID_ARGUMENT
                .withDescription("CEP com formato Inválido")
                .augmentDescription("Formato esperado deverá ser : 99999-999")
                .asRuntimeException()
            responseObserver?.onError(e)
        }

        //SIMULAR CENÁRIO ERRO - ERRO DE SEGURANÇA

        if (cep.endsWith("333")) {
            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.number)
                .setMessage("Usuário não pode acessar este recurso")
                .addDetails(
                    Any.pack(
                        ErrorDetails.newBuilder()
                            .setCode(401)
                            .setMessage("Token Expirado")
                            .build()
                    )
                )
                .build()

            val e = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver?.onError(e)
        }

        var valor = 0.0

        try {
            valor = Random.nextDouble(until = 140.00, from = 0.0)
            if (valor > 100) {
                throw IllegalArgumentException("Erro inesperado ao executar regra de negocio -- TESTE PARA LANÇAR EXCEPTION")
            }
        } catch (e: Exception) {
            responseObserver?.onError(
                Status.INTERNAL
                    .withDescription(e.message)
                    .withCause(e.cause) //metodo é anexado ao erro, no entanto não é enviado ao client -- apenas utilizqdo internamente
                    .asRuntimeException()
            )
        }


        val response = CalcularFretesResponse.newBuilder()
            .setCep(request.cep)
            .setValor(valor)
            .build()

        logger.info("Frete Calculado : $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }

}