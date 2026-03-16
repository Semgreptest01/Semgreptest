// *---------------------------------------------------------------------
// * Class_Name       : Dtime
// * システム　　名称 : ＬＥＴＳＳ管理会計
// * アプレット　名称 : リアルタイム日付時刻・表示
// * 会社名or所属名   : ローソン・システム
// * 作成日           : 00/02/17 14:37:55
// * 作成者           : Y.Takabayashi
// * *** 修正履歴 ***
// * No.  Date        Aouther      Description
// * 01   ____/__/__  _.________   _____________________________________
// *---------------------------------------------------------------------
import java.applet.*;
import java.awt.*;
import java.util.*;
public class Dtime extends Applet implements Runnable{
	Dimension d;
	int	fontsize, fontx = 0, fonty, type = 1, gmt = 9, fontstyle = 1;
	Color	color, backcolor = Color.white, fontcolor = Color.black;
	int	yyyy, mm, dd, hh, ms, ss;
	String	t, st_yyyy, st_mm, st_dd, st_hh, st_ms, st_ss, fontname = "TimesRoman";
	int	it = 1000, nWidth, nHeight;
	Thread th = null;
	public void init()
	{
		nWidth  = size().width;
		nHeight = size().height;
		resize(nWidth, nHeight);
                fontsize = nHeight - 1;
                fonty = nHeight - 1;
		String param;
		param = getParameter("fontname");  if (param != null) { fontname = param; }
		param = getParameter("fontstyle"); if (param != null) { fontstyle = Integer.parseInt(param); }
		param = getParameter("backcolor"); if (param != null) { backcolor = new Color(Integer.parseInt(param, 16)); }
		param = getParameter("type"); if (param != null) { type = Integer.parseInt(param); }
		param = getParameter("fontcolor"); if (param != null) { fontcolor = new Color(Integer.parseInt(param, 16)); }
		param = getParameter("fontsize"); if (param != null) { fontsize = Integer.parseInt(param); }
		param = getParameter("fontx"); if (param != null) { fontx = Integer.parseInt(param); }
		param = getParameter("fonty"); if (param != null) { fonty = Integer.parseInt(param); }
		param = getParameter("gmt"); if (param != null) { gmt = Integer.parseInt(param); }
	}
	public void start() {
		if (th==null){
			th=new Thread(this);
			th.start();
		}
		repaint();
	}
	public void paint(Graphics g) {
		String	tz[]=TimeZone.getAvailableIDs(gmt * 60 * 60 * 1000);
		Calendar d=Calendar.getInstance(TimeZone.getTimeZone(tz[0]));
		yyyy	= d.get(d.YEAR); st_yyyy = "";st_yyyy += yyyy;
		mm	= d.get(d.MONTH)+1; st_mm = "";st_mm += mm;
		dd	= d.get(d.DATE); st_dd = "";st_dd += dd;
		hh	= d.get(d.HOUR_OF_DAY); st_hh = "";st_hh += hh;
		ms	= d.get(d.MINUTE); st_ms = "";st_ms += ms;
		ss	= d.get(d.SECOND); st_ss = "";st_ss += ss;
		if( yyyy < 1000 ){ st_yyyy = "0" + yyyy; }
		if( yyyy < 100  ){ st_yyyy = "00" + yyyy; }
		if( yyyy < 10   ){ st_yyyy = "000" + yyyy; }
		if( mm < 10 ){ st_mm = "0" + mm; }
		if( dd < 10 ){ st_dd = "0" + dd; }
		if( hh < 10 ){ st_hh = "0" + hh; }
		if( ms < 10 ){ st_ms = "0" + ms; }
		if( ss < 10 ){ st_ss = "0" + ss; }
		setBackground(backcolor);
		g.setColor(fontcolor);
		g.setFont(new Font(fontname,Font.PLAIN,fontsize));
		if( fontstyle == 2 ){
			g.setFont(new Font(fontname,Font.BOLD,fontsize));
		}
		if( fontstyle == 3 ){
			g.setFont(new Font(fontname,Font.ITALIC,fontsize));
		}
//		g.drawString(st_yyyy + "/" + st_mm + "/" + st_dd + " " + st_hh + ":" + st_ms + ":" + st_ss,fontx,fonty);
		switch ( type ){
			case 1  : g.drawString(st_yyyy + "/" + st_mm + "/" + st_dd + " " + st_hh + ":" + st_ms + ":" + st_ss,fontx,fonty);break;
			case 2  : g.drawString(st_yyyy + "/" + st_mm + "/" + st_dd,fontx,fonty);break;
			case 3  : g.drawString(st_hh + ":" + st_ms + ":" + st_ss,fontx,fonty);break;
			case 4  : g.drawString(st_mm + "/" + st_dd,fontx,fonty);break;
			case 5  : g.drawString(st_hh + ":" + st_ms,fontx,fonty);break;
			default : g.drawString(st_yyyy + "/" + st_mm + "/" + st_dd + " " + st_hh + ":" + st_ms + ":" + st_ss,fontx,fonty);
		}
	}
	public void run() {
		while(true){
			try {
				th.sleep(it);
			}
			catch(InterruptedException e) {}
			repaint();
		}
	}
}
