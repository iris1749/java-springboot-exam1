package com.korea.textboard.domain;

import com.korea.textboard.base.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class ArticleController { // Model + Controller

    CommonUtil commonUtil = new CommonUtil();
    ArticleRepository articleRepository = new ArticleRepository();

    @RequestMapping("/search")
    @ResponseBody
    public ArrayList<Article> search(@RequestParam(value="keyword", defaultValue = "") String keyword) {

        ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);
        return searchedList;
    }

    @RequestMapping("/detail")
    public String detail(@RequestParam("articleId") int articleId, Model model) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        article.increaseHit();

        model.addAttribute("article", article);
        return "detail";
    }

    @RequestMapping("/delete/{articleId}")
    public String delete(@PathVariable("articleId") int articleId) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.deleteArticle(article);
        return "redirect:/list";
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(@RequestParam("articleId") int inputId,
                         @RequestParam("newTitle") String newTitle,
                         @RequestParam("newBody") String newBody
    ) {

        Article article = articleRepository.findArticleById(inputId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.updateArticle(article, newTitle, newBody);
        return "%d번 게시물이 수정되었습니다.".formatted(inputId);
    }

    @RequestMapping("/list")
    public String list(Model model) {

        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList", articleList);

        return "list";
    }

    // 실제 데이터 저장 처리 부분
    @PostMapping("/add")
    public String add(@RequestParam("title") String title,
                      @RequestParam("body") String body,
                      Model model) {

        articleRepository.saveArticle(title, body);

        // 문제 원인 : add 요청의 결과 화면을 list로 보여주고 있다.
        // 문제 해결 : add url을 list로 바꾸면 된다.
        // controller에서 주소를 바꾸는 법 : redirect
        return "redirect:/list"; // 브라우저의 주소가 /list로 바뀜

    }

    // 입력 화면 보여주기
    @GetMapping("/add")
    public String form() {
        return "form";
    }
}