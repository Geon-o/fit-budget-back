package jgo.dev.fit_budget.auth;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class NicknameGenerator {

    // 8자 이내, 재밌는 가계부 테마 닉네임
    private static final List<String> NICKNAMES = List.of(
            "짠돌이여우", "저축왕고양이", "예산요정", "돈모으는곰", "알뜰다람쥐",
            "가계부지킴이", "짠순이토끼", "부자되는중", "저금통펭귄", "돈세는판다",
            "알뜰살뜰이", "저축요정님", "가난탈출중", "짠테크고수", "돈길만걷자",
            "부자연습생", "저축마스터", "현금흐름왕", "가계부요정", "무지출챌린저"
    );

    private NicknameGenerator() {
    }

    public static String generate() {
        return NICKNAMES.get(ThreadLocalRandom.current().nextInt(NICKNAMES.size()));
    }
}
