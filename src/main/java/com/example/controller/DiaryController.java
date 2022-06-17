package com.example.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Diary;
import com.example.domain.Member;
import com.example.form.DiaryForm;
import com.example.form.MemberForm;
import com.example.repository.DiaryRepository;
import com.example.repository.MemberRepository;
import com.example.service.DiaryService;
import com.example.service.MemberService;

@Controller
@RequestMapping("/diary")
public class DiaryController {

	// 下準備
	@ModelAttribute
	public MemberForm setupMemberForm() {
		return new MemberForm();
	}

	@ModelAttribute
	public DiaryForm setupDiaryForm() {
		return new DiaryForm();
	}

	@Autowired
	MemberRepository memRepository;

	@Autowired
	DiaryRepository diRepository;

	@Autowired
	MemberService memService;

	@Autowired
	DiaryService diService;

	@Autowired
	private HttpSession session;

	// 本処理
	/*
	 * ログイン画面 ①既にログイン状態であればtop画面に遷移。 ②そうでなければログイン画面へ
	 */
	@RequestMapping("/login")
	public String login() {
		if (memService.loginjudge())
			return "redirect:/diary/top";
		return "login";
	}

	/*
	 * ログインチェック ①バリデーションチェック ②ログインが成功すれば、アカウント情報をセッションへ格納
	 * ③ログインが失敗すれば（sqlの戻りがnullだったら）正しいidとpasswordで再度ログインを促す
	 */
	@RequestMapping("/check")
	public String check(MemberForm memberForm, BindingResult result, RedirectAttributes redirectAttributes,
			Model model) {

		Member member = new Member();
		BeanUtils.copyProperties(memberForm, member);

		try {
			Member memberinfo = memService.login(member);
			session.setAttribute("memberinfo", memberinfo);
			return "redirect:/diary/top";
		} catch (EmptyResultDataAccessException e) {
			model.addAttribute("loginerror", "※id または passwordが間違っています");
			return login();
		}
	}

	/*
	 * トップ画面へ遷移 ①既にログイン状態であればtop画面に遷移。 ②そうでなければログイン画面へ
	 */
	@RequestMapping("/top")
	public String top() {
		return "top";
	}

	/*
	 * 新規会員登録画面 ①既にログイン状態であればtop画面に遷移。 ②そうでなければ新規会員登録画面へ
	 */
	@RequestMapping("/newmember")
	public String newmember() {
		return "newmember";
	}

	/*
	 * 新規会員登録 確認画面 ①バリデーションチェック ②既にログイン状態であればtop画面に遷移。 ③そうでなければ新規会員登録 確認画面へ
	 */
	@RequestMapping("/confirmation")
	public String confirmation(@Validated MemberForm memberForm, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			return newmember();
		}

		Member member = new Member();
		BeanUtils.copyProperties(memberForm, member);

		try {
			memService.findById(member.getId());
			model.addAttribute("alreadyMember", "※こちらのidは既に使われています");
			return newmember();
		} catch (EmptyResultDataAccessException e) {
			MemberForm temporaryMemberForm = memberForm;
			session.setAttribute("temporaryMemberForm", temporaryMemberForm);
			return "confirmation";
		}

	}

	/*
	 * 新規会員登録 完了画面。 ③新規会員登録をし、完了画面へ ④セッションにアカウント情報を入れる。
	 */
	@RequestMapping("/registrationcomplete")
	public String registrationcomplete() {
		MemberForm memberForm = (MemberForm) session.getAttribute("temporaryMemberForm");
		Member member = new Member();
		BeanUtils.copyProperties(memberForm, member);

		memService.insert(member);
		Member memberinfo = member;
		session.setAttribute("memberinfo", memberinfo);
		session.removeAttribute("temporaryMemberForm");
		return "registrationcomplete";
	}

	/*
	 * アカウント情報表示画面 ①ログイン状態の判定 ②アカウント情報表示
	 */
	@RequestMapping("/accountinfo")
	public String accountinfo() {
		if (memService.loginjudge() == false)
			return "redirect:/diary/login";

		return "accountinfo";
	}

	/*
	 * ログアウト
	 */
	@RequestMapping("/logout")
	public String logout() {
		session.removeAttribute("memberinfo");
		return "logout";
	}

	/*
	 * 退会
	 */
	@RequestMapping("/withdrawal")
	public String withdrawal() {
		return "redirect:/diary/withdrawalconfirmation";
	}

	/*
	 * 退会確認
	 */
	@RequestMapping("/withdrawalconfirmation")
	public String withdrawalconfirmation() {
		return "withdrawalconfirmation";
	}

	/*
	 * 退会完了
	 */
	@RequestMapping("/withdrawalcompletion")
	public String withdrawalcompletion() {
		Member memberinfo = (Member) session.getAttribute("memberinfo");
		String memberId = memberinfo.getId();
		// diary memberIdで削除
		diService.deleteByMemberId(memberId);
		// member idで削除
		memService.deleteById(memberId);
		// sessionの削除
		session.removeAttribute("memberinfo");
		return "withdrawalcompletion";
	}

	/*
	 * 日記記入画面 ①ログイン状態の判定 ②セッションにdiaryListがなかったら作る
	 */
	@RequestMapping("/diarywrite")
	public String diaryWrite() {
		if (memService.loginjudge() == false)
			return "redirect:/diary/login";

		List<Diary> diaryList = (List<Diary>) session.getAttribute("diaryList");
		if (diaryList == null) {
			diaryList = new ArrayList<>();
			session.setAttribute("diaryList", diaryList);
		}
		return "diary";
	}

	/*
	 * 日記記入チェック画面 ①ログイン状態の判定 ②form(String)→diary(sql.date)に変換 ③DiaryへInsert処理
	 * ②セッションにあるdiaryListに追加
	 */
	@RequestMapping("/alldiarycheck")
	public String alldiarycheck(@Validated DiaryForm diaryForm, BindingResult result, RedirectAttributes redirectAttributes) {
		if(result.hasErrors())return diaryWrite();
		if (memService.loginjudge() == false)return "redirect:/diary/login";

		Diary diary = new Diary();
		BeanUtils.copyProperties(diaryForm, diary);

		String strdate = diaryForm.getDate();
		Date sqldate = Date.valueOf(strdate);
		diary.setDate(sqldate);

		Member member = (Member) session.getAttribute("memberinfo");
		String memberId = member.getId();
		diary.setMemberId(memberId);

		diService.insert(diary);

		List<Diary> diaryList = diService.findByMemberId(memberId);

		session.setAttribute("diaryList", diaryList);
		return "redirect:/diary/alldiary";
	}

	/*
	 * 日記編集チェック画面 ②form(String)→diary(sql.date)に変換 ③Diaryへupdate処理
	 * ②セッションにあるdiaryListに追加
	 */
	@RequestMapping("/updatealldiarycheck")
	public String updatealldiarycheck(DiaryForm diaryForm, RedirectAttributes redirectAttributes) {
		if (memService.loginjudge() == false)
			return "redirect:/diary/login";

		Diary diary = new Diary();
		BeanUtils.copyProperties(diaryForm, diary);

		String strdate = diaryForm.getDate();
		Date sqldate = Date.valueOf(strdate);
		diary.setDate(sqldate);

		Member member = (Member) session.getAttribute("memberinfo");
		String memberId = member.getId();
		diary.setMemberId(memberId);

		Diary temporaryDiary = (Diary) session.getAttribute("temporaryDiary");
		Integer diaryId = temporaryDiary.getId();
		diary.setId(diaryId);

		diService.update(diary);

		List<Diary> diaryList = diService.findByMemberId(memberId);

		session.setAttribute("diaryList", diaryList);
		session.removeAttribute("temporaryDiary");
		return "redirect:/diary/alldiary";
	}

	/*
	 * 日記一覧画面 ①ログイン状態の判定 ②これまで書いた日記一覧表示
	 */
	@RequestMapping("alldiary")
	public String alldiary(Model model) {
		if (memService.loginjudge() == false)
			return "redirect:/diary/login";

		Member member = (Member) session.getAttribute("memberinfo");
		String memberId = member.getId();
		List<Diary> diaryList = diService.findByMemberId(memberId);
		session.setAttribute("diaryList", diaryList);

		return "alldiary";
	}

	/*
	 * 日記編集画面 ①ログイン状態の判定 ②日記編集画面表示
	 */
	@RequestMapping("/editing")
	public String editing(Integer diaryId) {
		Diary temporaryDiary = diService.findById(diaryId);
		session.setAttribute("temporaryDiary", temporaryDiary);
		return "editing";
	}

	/*
	 * 日記削除完了 ②日記削除完了画面表示
	 */
	@RequestMapping("/diarydelete")
	public String diaryDelete(Integer diaryId) {
		System.out.println(diaryId);
		diService.delete(diaryId);
		return "diarydelete";
	}

}
