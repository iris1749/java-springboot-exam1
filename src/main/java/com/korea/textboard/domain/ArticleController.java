






















































































































































































































































































































































































































































































































































































































package com.korea.textboard.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.textboard.base.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Scanner;

// Model - Controller - View
@Controller
public class ArticleController { // Model + Controller

    CommonUtil commonUtil = new CommonUtil();
    ArticleView articleView = new ArticleView();
    ArticleRepository articleRepository = new ArticleRepository();

    Scanner scan = commonUtil.getScanner();
    int WRONG_VALUE = -1;


    @RequestMapping("/search")
    @ResponseBody
    public ArrayList<Article> search(@RequestParam(value="keyword", defaultValue = "") String keyword) {
        ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);

        return searchedList;
    }

    @RequestMapping("/detail")
    @ResponseBody
    public String detail(@RequestParam("articleId") int articleId) {
        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        article.increaseHit();
        String jsonString = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("articleId") int inputId) {

        Article article = articleRepository.findArticleById(inputId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.deleteArticle(article);
        return "%d번 게시물이 삭제되었습니다.".formatted(inputId);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(@RequestParam("articleId") int inputId,
                         @RequestParam("newTitle") String newTitle,
                         @RequestParam("newBody") String newBody
    ) {
//        System.out.print("수정할 게시물 번호를 입력해주세요 : ");
//
//        int inputId = getParamAsInt(scan.nextLine(), WRONG_VALUE);
//        if(inputId == WRONG_VALUE) {
//            return;
//        }

        Article article = articleRepository.findArticleById(inputId);

        if (article == null) {
//            System.out.println("없는 게시물입니다.");
            return "없는 게시물입니다.";
        }

//        System.out.print("새로운 제목을 입력해주세요 : ");
//        String newTitle = scan.nextLine();
//
//        System.out.print("새로운 내용을 입력해주세요 : ");
//        String newBody = scan.nextLine();

        articleRepository.updateArticle(article, newTitle, newBody);
        return "%d번 게시물이 수정되었습니다.".formatted(inputId);
    }


    @RequestMapping("/list")
    @ResponseBody
    public ArrayList<Article> list() {
        ArrayList<Article> articleList = articleRepository.findAll();

        return articleList;
//        articleView.printArticleList(articleList); // 전체 출력 -> 전체 저장소 넘기기
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add(@RequestParam("title") String title,
                      @RequestParam("body") String body) {

//        System.out.print("게시물 제목을 입력해주세요 : ");
//        String title = scan.nextLine();
//
//        System.out.print("게시물 내용을 입력해주세요 : ");
//        String body = scan.nextLine();

        articleRepository.saveArticle(title, body);
//        System.out.println("게시물이 등록되었습니다.");
        return "게시물이 등록되었습니다.";

    }

    private int getParamAsInt(String param, int defaultValue) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해주세요.");
            return defaultValue;
        }
    }
}