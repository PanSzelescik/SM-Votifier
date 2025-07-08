package pl.ibcgames.smvotifier.response;

import java.util.List;

public record VoteResponse(int id,
                           List<String> text,
                           String voteUrl) {
}
