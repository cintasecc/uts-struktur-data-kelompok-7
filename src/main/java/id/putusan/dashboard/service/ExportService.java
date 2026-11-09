package id.putusan.dashboard.service;
import id.putusan.dashboard.repository.PutusanRepository;import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.*;
/** Ekspor CSV dan laporan TXT. */
public class ExportService {private final PutusanRepository repo;public ExportService(PutusanRepository r){repo=r;}public void csv(Path p)throws IOException{repo.save(p);}public void txt(Path p,String summary)throws IOException{Files.writeString(p,summary,StandardCharsets.UTF_8);}}
