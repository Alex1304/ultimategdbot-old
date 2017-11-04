package ultimategdbot.util;

public enum Emoji {
	STAR("<:Star:376445841885495316>"),
	DEMON("<:Demon:376444770186166273>"),
	MODERATOR("<:Moderator:376444770542944257>"),
	PLAY("<:Play:364096635019722764>"),
	LENGTH("<:Length:364077721565003786>"),
	DOWNLOADS("<:Downloads:364076905130885122>"),
	LIKE("<:Like:364076087648452610>"),
	DISLIKE("<:Dislike:364076032602406912>"),
	DIAMOND("<:Diamond:376444770613985280>"),
	USERCOIN("<:UserCoin:376444770438086667>"),
	SECRETCOIN("<:SecretCoin:376444770156937228>"),
	INFO("<:Info:364092562040160266>"),
	CREATOR_POINTS("<:CreatorPoints:364075937857273867>"),
	GLOBAL_RANK("<:GlobalRank:376455545353273366>"),
	YOUTUBE("<:YouTube:376469522661507072>"),
	TWITCH("<:Twitch:376469536653836302>"),
	TWITTER("<:Twitter:376469544480407563>");
	
	private String code;
	
	private Emoji(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return code;
	}
	
}
