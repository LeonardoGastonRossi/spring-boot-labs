package io.pivotal.workshop;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/snippets")
public class SnippetController {
    private final SnippetRepository snippetRepository;
    private final SnippetPresenter snippetPresenter;

    public SnippetController(SnippetRepository snippetRepository, SnippetPresenter snippetPresenter) {
        this.snippetRepository = snippetRepository;
        this.snippetPresenter = snippetPresenter;
    }

    @GetMapping
    public List<SnippetInfo> snippets() {
        return snippetRepository.findAll()
                .stream()
                .map(snippetPresenter::present)
                .collect(Collectors.toList());
    }

    @GetMapping("/between/{date1}/{date2}")
    public List<SnippetInfo> between(@PathVariable("date1") String startDate,@PathVariable("date2") String endDate) {
        return snippetRepository.findBetween(startDate, endDate)
                .stream()
                .map(snippetPresenter::present)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SnippetInfo snippet(@PathVariable("id") String id) {
        SnippetRecord record = snippetRepository.findOne(id);

        return snippetPresenter.present(record);
    }

    @PostMapping
    public ResponseEntity<SnippetInfo> add(@RequestBody NewSnippetFields newSnippetFields) {
        SnippetRecord savedRecord = snippetRepository.save(newSnippetFields);

        SnippetInfo savedSnippetInfo = snippetPresenter.present(savedRecord);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(buildSnippetUri(savedSnippetInfo));

        return new ResponseEntity<SnippetInfo>(savedSnippetInfo, httpHeaders, HttpStatus.CREATED);
    }

    private URI buildSnippetUri(SnippetInfo snippet) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/" + snippet.getId())
                .buildAndExpand().toUri();
    }

}