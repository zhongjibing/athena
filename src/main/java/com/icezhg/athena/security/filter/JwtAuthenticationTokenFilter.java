package com.icezhg.athena.security.filter;

/**
 * Created by zhongjibing on 2020/04/27
 */
public class JwtAuthenticationTokenFilter //extends OncePerRequestFilter
{

//    public static final String BEARER = "Bearer ";
//
//    public static final String AUTHORIZATION = "Authorization";
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    public JwtAuthenticationTokenFilter(JwtTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String jwt = resolveToken(request);
//
//        if (StringUtils.isNotBlank(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (this.jwtTokenProvider.validate(jwt)) {
//                Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader(AUTHORIZATION);
//        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
//            return bearerToken.substring(BEARER.length());
//        }
//        String jwt = request.getParameter(AUTHORIZATION);
//        if (org.springframework.util.StringUtils.hasText(jwt)) {
//            return jwt;
//        }
//        return null;
//    }
}
