package my.photomanager.web.response;

import java.util.List;

public record PhotoResponse(String externalId, List<TagResponse> tags) {
}
