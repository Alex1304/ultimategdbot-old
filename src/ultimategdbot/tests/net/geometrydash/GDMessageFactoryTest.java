package ultimategdbot.tests.net.geometrydash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ultimategdbot.net.geometrydash.GDMessage;
import ultimategdbot.net.geometrydash.GDMessageFactory;
import ultimategdbot.net.geometrydash.GDRole;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.net.geometrydash.GDUserFactory;

/**
 * Tests for class {@link GDMessageFactory}
 * 
 * @author Alex1304
 */
public class GDMessageFactoryTest {

	private String oneMessageRawData, allMessagesRawData;
	private final GDUser botUser = GDUserFactory.getBotUserInstance();
	
	@Before
	public void setUp() throws Exception {
		oneMessageRawData = "6:Alex1304:3:4063664:2:98006:1:31188579:4:dGVzdA==:8:1:9:0:5:RVFBQQ==:7:15 hours";
		allMessagesRawData = "6:Gd Captain:3:14734551:2:5501643:1:31203425:4:SGVsbG8gZnJpZW5kIDwz:8:1:9:0:7:2 hours|6:scripen:3:5606070:2:882169:1:31199515:4:Q29sbGFiPw==:8:1:9:0:7:3 hours|6:firecharlyyt:3:20830624:2:5983766:1:31172967:4:dXIgYWdl:8:1:9:0:7:19 hours|6:OllieIsTheBest:3:57835185:2:7910160:1:31169160:4:UmU6IENhbiBJIGhhdmUgdGhlIFBhc3N3b3JkIG9mIEtvdG8gU28g:8:1:9:0:7:1 day|6:thepro3:3:36951393:2:7989944:1:31166941:4:Z2xvdw==:8:1:9:0:7:1 day|6:Nebs12:3:6803327:2:5473208:1:31155009:4:d2FpdA==:8:1:9:0:7:1 day|6:Fumenu:3:15073633:2:4868299:1:31152496:4:R0M=:8:1:9:0:7:1 day|6:danilpro12567:3:58660847:2:8005003:1:31118233:4:b29vbw==:8:1:9:0:7:3 days|6:alexandre85:3:20243073:2:5900841:1:31101887:4:UmU6IGxvbA==:8:1:9:0:7:3 days|6:Vexade:3:4041456:2:916286:1:31100634:4:S290b3J1cHRpb24=:8:1:9:0:7:3 days|6:alexandre85:3:20243073:2:5900841:1:31097767:4:UmU6IGxvbA==:8:1:9:0:7:3 days|6:alexandre85:3:20243073:2:5900841:1:31097745:4:UmU6IGxvbA==:8:1:9:0:7:3 days|6:SuperstarGD:3:37667754:2:6995223:1:31092714:4:Lg==:8:1:9:0:7:4 days|6:Spectre11:3:37175883:2:7503605:1:31075833:4:TGV2ZWw=:8:1:9:0:7:4 days|6:Spectre11:3:37175883:2:7503605:1:31075832:4:TGV2ZWw=:8:1:9:0:7:4 days|6:Blockfighter15:3:48001153:2:7657972:1:31075781:4:cGxlYXNlIHJlYWQgPDM=:8:1:9:0:7:4 days|6:ArtUrIk67:3:31980026:2:6453922:1:31070098:4:TGV2ZWw=:8:1:9:0:7:4 days|6:iIi69Derp69iIi:3:3248962:2:1664154:1:31069050:4:SWdub3JlIHRoZSBoYXRlcnM=:8:1:9:0:7:5 days|6:Gd Captain:3:14734551:2:5501643:1:31047958:4:SGVsbG8gZHVkZSA6KQ==:8:1:9:0:7:5 days|6:RadiantEnergy:3:5936659:2:854282:1:31040123:4:dGhlIGJlc3QgY3JlYXRvciBldmVy:8:1:9:0:7:6 days|6:CosmicLuigi:3:11841215:2:2709130:1:31036196:4:aGV5:8:1:9:0:7:6 days|6:ThreeLNEX:3:31752217:2:6687209:1:31027111:4:RmF2b3I_:8:1:9:0:7:6 days|6:OllieIsTheBest:3:57835185:2:7910160:1:31025334:4:Q2FuIEkgaGF2ZSB0aGUgUGFzc3dvcmQgb2YgS290byBTbyA=:8:1:9:0:7:6 days|6:Visionary Galax:3:58236587:2:7950967:1:31023816:4:UGxlYXNlIHJlYWQu:8:1:9:0:7:6 days|6:enery:3:19819055:2:5966929:1:31022617:4:ISEh:8:1:9:0:7:6 days|6:nathan11:3:58537210:2:7985826:1:31015563:4:eW91ciBsZXZlbCA=:8:1:9:0:7:6 days|6:krisz:3:14098799:2:3902752:1:30994768:4:Oik=:8:1:9:0:7:1 week|6:nando11:3:27835151:2:6459788:1:30992486:4:dG8gYWxleA==:8:1:9:0:7:1 week|6:Wertstriker:3:19298600:2:5716787:1:30989735:4:UmU6IFlvdSBmb3Jnb3Qgc29tZXRoaW5nLg==:8:1:9:0:7:1 week|6:Wertstriker:3:19298600:2:5716787:1:30989733:4:UmU6IFlvdSBmb3Jnb3Qgc29tZXRoaW5nLg==:8:1:9:0:7:1 week|6:Wertstriker:3:19298600:2:5716787:1:30989127:4:WW91IGZvcmdvdCBzb21ldGhpbmcu:8:1:9:0:7:1 week|6:customXelody:3:16585425:2:5339581:1:30988664:4:ZnVycnlz:8:1:9:0:7:1 week|6:PrayForEpiic:3:11847479:2:2611779:1:30987923:4:UmU6IE5vcGU=:8:1:9:0:7:1 week|6:Cergouisan RBP:3:14031295:2:5178073:1:30987027:4:aGV5:8:1:9:0:7:1 week|6:Cergouisan RBP:3:14031295:2:5178073:1:30987026:4:aGV5:8:1:9:0:7:1 week|6:mulpan:3:3112019:2:119703:1:30985104:4:Z2cga290bw==:8:1:9:0:7:1 week|6:ThreeLNEX:3:31752217:2:6687209:1:30984604:4:UmU6IFNvcnJ5Lg==:8:1:9:0:7:1 week|6:GD junior07:3:21759628:2:6221208:1:30984183:4:cGxlYXNl:8:1:9:0:7:1 week|6:RiddleFiddleGD:3:10318979:2:1702348:1:30983371:4:SGVsbG8=:8:1:9:0:7:1 week|6:ThreeLNEX:3:31752217:2:6687209:1:30982612:4:U29ycnku:8:1:9:0:7:1 week|6:Multibot:3:57900220:2:7916460:1:30979827:4:MjE4MDk4LUxJTktBQ0M=:8:1:9:0:7:1 week|6:Sebs2002:3:27404313:2:7474051:1:30979325:4:UmU6IENvbmdyYXR1bGF0aW9ucyE=:8:1:9:0:7:1 week|6:Sebs2002:3:27404313:2:7474051:1:30978073:4:UmU6IENvbmdyYXR1bGF0aW9ucyE=:8:1:9:0:7:1 week|6:FrancaDash:3:13280786:2:3503623:1:30977360:4:Q2FuIHUgc2VlIG15IGxldmxlIHRiaD8g:8:1:9:0:7:1 week|6:vragera:3:30835214:2:6376735:1:30975552:4:aGV5ISBGZWF0dXJlPw==:8:1:9:0:7:1 week|6:Leptadox:3:13247132:2:3482594:1:30975090:4:SGVscA==:8:1:9:0:7:1 week|6:JDBMuoz:3:57895361:2:7973998:1:30974143:4:aG9sYQ==:8:1:9:0:7:1 week|6:Tickle GD:3:12908486:2:3299588:1:30963265:4:UmU6IFF3cSAv:8:1:9:0:7:1 week|6:MRflippy:3:8990834:2:1394390:1:30961106:4:TVJmbGlwcHk=:8:1:9:0:7:1 week|6:MRflippy:3:8990834:2:1394390:1:30961092:4:TVJmbGlwcHk=:8:1:9:0:7:1 week#435:0:";
	}

	@Test
	public void test_buildAllGDMessagesFromFirstPage() throws Exception {
		// Visual verification
		System.out.println(GDMessageFactory.buildAllGDMessagesFromFirstPage(allMessagesRawData));
	}
	
	@Test
	public void test_buildOneGDMessage() throws Exception {
		GDMessage expected = new GDMessage(31188579,
				new GDUser("", 0, 0, 0, 0, 0, 0, 0, "", 0, 98006, GDRole.USER, "", ""),
				botUser, "test", "test");
		
		assertEquals("MessageID", expected.getId(), GDMessageFactory.buildOneGDMessage(oneMessageRawData).getId());
		assertEquals("Message author", expected.getAuthor(), GDMessageFactory.buildOneGDMessage(oneMessageRawData).getAuthor());
		assertEquals("Message recipient", expected.getRecipient(), GDMessageFactory.buildOneGDMessage(oneMessageRawData).getRecipient());
		assertEquals("Message subject", expected.getSubject(), GDMessageFactory.buildOneGDMessage(oneMessageRawData).getSubject());
		assertEquals("Message body", expected.getBody(), GDMessageFactory.buildOneGDMessage(oneMessageRawData).getBody());
	}

}
