import java.util.List;
import java.util.Map;

public class Input {
	public final int V;
	public final int E;
	public final int R;
	public final int C;
	public final int X;
	public final int[] Vs;
	public final Endpoint Es;
	public final Request[] Rs;

	public Input(int v, int e, int r, int c, int x, int[] vs, Endpoint es, Request[] rs) {
		V = v;
		E = e;
		R = r;
		C = c;
		X = x;
		Vs = vs;
		Es = es;
		Rs = rs;
	}

	public static class Endpoint {
		public final int Ld;
		public final int K;
		public final Map<Integer, Integer> L;

		public Endpoint(int ld, int k, Map<Integer, Integer> l) {
			Ld = ld;
			K = k;
			L = l;
		}
	}

	public static class Request {
		public final int Rv;
		public final int Re;
		public final int Rn;

		public Request(int rv, int re, int rn) {
			Rv = rv;
			Re = re;
			Rn = rn;
		}
	}
}