package delivery.reference;

import delivery.grpc.CalculateTimeRequest;
import delivery.grpc.CalculateTimeResponse;
import delivery.grpc.GetZoneBasePriceRequest;
import delivery.grpc.GetZoneBasePriceResponse;
import delivery.grpc.ReferenceServiceGrpc;
import delivery.grpc.ValidateZoneRequest;
import delivery.grpc.ValidateZoneResponse;
import io.grpc.stub.StreamObserver;

/**
 * Service B: справочник зон, базовых цен и эталонного времени доставки.
 */
public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {

    @Override
    public void validateZone(ValidateZoneRequest request, StreamObserver<ValidateZoneResponse> responseObserver) {
        String traceId = request.getTraceId();
        String zone = request.getZone();
        if (zone == null) {
            zone = "";
        }
        zone = zone.trim().toUpperCase();
        log("validateZone", traceId, "zone=" + zone);

        boolean known = zone.equals("CENTER")
                || zone.equals("NORTH")
                || zone.equals("SOUTH")
                || zone.equals("EAST")
                || zone.equals("WEST");
        ValidateZoneResponse response = ValidateZoneResponse.newBuilder()
                .setValid(known)
                .setMessage(known ? "OK" : "Неизвестная зона доставки")
                .build();

        log("validateZone", traceId, "result=" + known);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void calculateDeliveryTime(CalculateTimeRequest request, StreamObserver<CalculateTimeResponse> responseObserver) {
        String traceId = request.getTraceId();
        String zone = request.getZone();
        if (zone == null) {
            zone = "";
        }
        zone = zone.trim().toUpperCase();
        log("calculateDeliveryTime", traceId, "zone=" + zone);

        int minutes = 45;
        if (zone.equals("CENTER")) {
            minutes = 20;
        } else if (zone.equals("NORTH")) {
            minutes = 30;
        } else if (zone.equals("SOUTH")) {
            minutes = 25;
        } else if (zone.equals("EAST") || zone.equals("WEST")) {
            minutes = 40;
        }

        CalculateTimeResponse response = CalculateTimeResponse.newBuilder()
                .setMinutes(minutes)
                .build();

        log("calculateDeliveryTime", traceId, "minutes=" + minutes);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getZoneBasePrice(GetZoneBasePriceRequest request, StreamObserver<GetZoneBasePriceResponse> responseObserver) {
        String traceId = request.getTraceId();
        String zone = request.getZone();
        if (zone == null) {
            zone = "";
        }
        zone = zone.trim().toUpperCase();
        log("getZoneBasePrice", traceId, "zone=" + zone);

        double price = 0.0;
        boolean found = true;
        if (zone.equals("CENTER")) {
            price = 5.0;
        } else if (zone.equals("NORTH")) {
            price = 7.0;
        } else if (zone.equals("SOUTH")) {
            price = 6.0;
        } else if (zone.equals("EAST")) {
            price = 8.0;
        } else if (zone.equals("WEST")) {
            price = 10.0;
        } else {
            found = false;
        }

        GetZoneBasePriceResponse response = GetZoneBasePriceResponse.newBuilder()
                .setFound(found)
                .setBasePrice(price)
                .build();

        log("getZoneBasePrice", traceId, "found=" + response.getFound());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void log(String rpc, String traceId, String detail) {
        System.out.println("[Reference] " + rpc + " traceId=" + traceId + " " + detail);
    }
}
