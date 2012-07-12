package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Foursquare2Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Servlet implementation class OAuthServlet
 */
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private OAuthService service;

	/**
	 * Default constructor.
	 */
	public OAuthServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(null, verifier);
		OAuthRequest req = new OAuthRequest(Verb.POST,
				"https://api.foursquare.com/v2/checkins/add");
		req.addBodyParameter("venueId", "4db97a14a86ee4fa7d388e78");
		req.addBodyParameter("oauth_token", accessToken.getToken());
		service.signRequest(accessToken, req);
		Response resp = req.send();
		System.out.println(resp.getBody());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		service = new ServiceBuilder()
					.provider(Foursquare2Api.class)
					.apiKey("VQQJMVSHX1S2ODDH4FH3FLYYDFAITI144DENTQX5U50DBY0Y")
					.apiSecret(
							"YKZDCFRTCOMAETJ33YMYVGQ5VOHQNQQZ3OCGM3OCGD0RN2R4")
					.callback("http://172.25.28.53:8080/FourSquareClient/oauth")
					.debug().build();
			Token requestToken = null;
			response.sendRedirect(service.getAuthorizationUrl(requestToken));
	}

}
