import java.util.Map;

public class Input {
	public final int V;
	public final int E;
	public final int R;
	public final int C;
	public final int X;
	public final int[] videoSizes;
	public final Endpoint[] Es;
	public final Request[] Rs;

	public Input(int v, int e, int r, int c, int x, int[] vs, Endpoint[] es, Request[] rs) {
		V = v;
		E = e;
		R = r;
		C = c;
		X = x;
		videoSizes = vs;
		Es = es;
		Rs = rs;
	}

	public static class Endpoint {
		public final int id;
		public final int latency; // latencia del data center
		public final int K;
		public final Map<Integer, Integer> caches; // cache -> latencia

		public Endpoint(int id, int ld, int k, Map<Integer, Integer> l) {
			this.id = id;
			latency = ld;
			K = k;
			caches = l;
		}
	}

	public static class Request {
		public final int videoId;
		public final int endpointId;
		public final int R;

		public Request(int rv, int re, int rn) {
			videoId = rv;
			endpointId = re;
			R = rn;
		}
	}
}