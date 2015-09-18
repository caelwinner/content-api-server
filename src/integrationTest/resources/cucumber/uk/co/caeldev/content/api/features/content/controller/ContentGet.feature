Feature: Content

  Scenario Outline: As a consumer, I should be able to get content by UUID that belong to a particular publisher.
    Given a publisher with id <publisher_id>, username <username> and publisher uuid <publisher_uuid>
    And credential details have been provided using username <credential_username>
    And the following persisted content associated to a publisher
      | contentUUID                          | publisherId                          | content           | status |
      | d72b4c53-8993-4a5f-acc2-629456e81c6d | f5c40191-22ec-4d34-9d8d-2cb9732635fc | http://google.com | UNREAD |
      | 86e0a3f8-9871-41ab-a30a-a3c57f6032a1 | 4890775a-f0e7-4f22-b482-18cd75de7221 | http://google.com | UNREAD |
      | d244fce6-54f6-47e4-8eae-12150ae1791e | 13163bf2-0c9b-4764-b8f6-f752a77cb131 | http://google.com | UNREAD |
    When get a content by UUID <content_uuid> and publisher UUID <publisher_uuid>
    Then the content get response is <status_code>

    Examples:
      | status_code | credential_username | username | publisher_uuid                       | publisher_id                         | content_uuid                         |
      | 200         | usertest            | usertest | 6cf1baaf-56fe-4e3b-844d-c73782d58917 | f5c40191-22ec-4d34-9d8d-2cb9732635fc | d72b4c53-8993-4a5f-acc2-629456e81c6d |
      | 403         | usertest            | usertest | 13163bf2-0c9b-4764-b8f6-f752a77cb131 | 13163bf2-0c9b-4764-b8f6-f752a77cb131 | d72b4c53-8993-4a5f-acc2-629456e81c6d |
      | 403         | usertest1           | usertest | 6cf1baaf-56fe-4e3b-844d-c73782d58917 | f5c40191-22ec-4d34-9d8d-2cb9732635fc | d72b4c53-8993-4a5f-acc2-629456e81c6d |

  Scenario Outline: As a consumer, I should be able to get all content by publisher UUID and status paginated.
    Given a publisher with id <publisher_id>, username <username> and publisher uuid <publisher_uuid>
    And credential details have been provided using username <credential_username>
    And the following persisted content associated to a publisher
      | contentUUID                          | publisherId                          | content           | status |
      | d72b4c53-8993-4a5f-acc2-629456e81c6d | f5c40191-22ec-4d34-9d8d-2cb9732635fc | http://google.com | UNREAD |
      | 86e0a3f8-9871-41ab-a30a-a3c57f6032a1 | f5c40191-22ec-4d34-9d8d-2cb9732635fc | http://google.com | UNREAD |
      | d244fce6-54f6-47e4-8eae-12150ae1791e | f5c40191-22ec-4d34-9d8d-2cb9732635fc | http://google.com | UNREAD |
    When get <number_of_pages> pages of size <page_size> by status <status> and publisher UUID <publisher_uuid>
    Then the number of pages is <number_of_pages>
    And all pages contains <page_size> content

    Examples:
      | credential_username | username | publisher_uuid                       | publisher_id                         | status | page_size | number_of_pages |
      | usertest            | usertest | 6cf1baaf-56fe-4e3b-844d-c73782d58917 | f5c40191-22ec-4d34-9d8d-2cb9732635fc | UNREAD | 1         | 3               |