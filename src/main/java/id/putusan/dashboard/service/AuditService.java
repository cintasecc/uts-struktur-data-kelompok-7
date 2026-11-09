package id.putusan.dashboard.service;
import id.putusan.dashboard.ds.*;import java.time.LocalDateTime;
/** Audit aktivitas dengan Stack custom. */
public class AuditService {private final Stack<AuditEntry> log=new Stack<>();public void record(String user,String action,String detail){log.push(new AuditEntry(LocalDateTime.now(),user,action,detail));}public String history(){return log.restore();}public int size(){return log.size();}}
