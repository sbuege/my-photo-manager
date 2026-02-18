package my.photomanager.web.response;

import java.util.List;

public record PhotoResponse(long id, List<TagResponse> tags) {
}
