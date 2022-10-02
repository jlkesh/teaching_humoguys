package uz.jl.test;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;

@Getter
public class AppResponse<T> {
    private Integer status;
    private boolean ok;
    private T body;
    private Long total;
    private Timestamp timestamp;
    private AppErrorDTO error;

    public AppResponse(T body) {
        this.body = body;
        this.ok = true;
        this.status = 200;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
    }

    public AppResponse(T body, Integer status) {
        this.body = body;
        this.ok = true;
        this.status = status;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
    }


    public AppResponse(T body, Long total) {
        this.body = body;
        this.ok = true;
        this.status = 200;
        this.total = total;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
    }

    public AppResponse(T body, Integer status, Long total) {
        this.body = body;
        this.ok = true;
        this.status = status;
        this.total = total;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
    }

    public AppResponse(AppErrorDTO error) {
        this.ok = false;
        this.error = error;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()));
    }


}
